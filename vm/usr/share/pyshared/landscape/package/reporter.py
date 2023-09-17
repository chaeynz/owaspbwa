import urlparse
import logging
import time
import sys
import os

from twisted.internet.defer import Deferred, succeed
from twisted.internet.utils import getProcessOutputAndValue

from landscape.lib.sequenceranges import sequence_to_ranges
from landscape.lib.twisted_util import gather_results
from landscape.lib.fetch import fetch_async
from landscape.lib.fs import touch_file

from landscape.package.taskhandler import (
    PackageTaskHandlerConfiguration, PackageTaskHandler, run_task_handler)
from landscape.package.store import UnknownHashIDRequest


HASH_ID_REQUEST_TIMEOUT = 7200
MAX_UNKNOWN_HASHES_PER_REQUEST = 500


class PackageReporterConfiguration(PackageTaskHandlerConfiguration):
    """Specialized configuration for the Landscape package-reporter."""

    def make_parser(self):
        """
        Specialize L{Configuration.make_parser}, adding options
        reporter-specific options.
        """
        parser = super(PackageReporterConfiguration, self).make_parser()
        parser.add_option("--force-smart-update", default=False,
                          action="store_true",
                          help="Force running smart-update.")
        return parser


class PackageReporter(PackageTaskHandler):
    """Report information about the system packages.

    @cvar queue_name: Name of the task queue to pick tasks from.
    @cvar smart_update_interval: Time interval in minutes to pass to
        the C{--after} command line option of C{smart-update}.
    """
    config_factory = PackageReporterConfiguration

    queue_name = "reporter"

    smart_update_interval = 60
    smart_update_filename = "/usr/lib/landscape/smart-update"
    sources_list_filename = "/etc/apt/sources.list"
    sources_list_directory = "/etc/apt/sources.list.d"

    def run(self):
        result = Deferred()

        # Run smart-update before anything else, to make sure that
        # the SmartFacade will load freshly updated channels
        result.addCallback(lambda x: self.run_smart_update())

        # If the appropriate hash=>id db is not there, fetch it
        result.addCallback(lambda x: self.fetch_hash_id_db())

        # Attach the hash=>id database if available
        result.addCallback(lambda x: self.use_hash_id_db())

        # Now, handle any queued tasks.
        result.addCallback(lambda x: self.handle_tasks())

        # Then, remove any expired hash=>id translation requests.
        result.addCallback(lambda x: self.remove_expired_hash_id_requests())

        # After that, check if we have any unknown hashes to request.
        result.addCallback(lambda x: self.request_unknown_hashes())

        # Finally, verify if we have anything new to report to the server.
        result.addCallback(lambda x: self.detect_changes())

        result.callback(None)
        return result

    def fetch_hash_id_db(self):
        """
        Fetch the appropriate pre-canned database of hash=>id mappings
        from the server. If the database is already present, it won't
        be downloaded twice.

        The format of the database filename is <uuid>_<codename>_<arch>,
        and it will be downloaded from the HTTP directory set in
        config.package_hash_id_url, or config.url/hash-id-databases if
        the former is not set.

        Fetch failures are handled gracefully and logged as appropriate.
        """

        def fetch_it(hash_id_db_filename):

            if hash_id_db_filename is None:
                # Couldn't determine which hash=>id database to fetch,
                # just ignore the failure and go on
                return

            if os.path.exists(hash_id_db_filename):
                # We don't download twice
                return

            base_url = self._get_hash_id_db_base_url()
            if not base_url:
                logging.warning("Can't determine the hash=>id database url")
                return

            # Cast to str as pycurl doesn't like unicode
            url = str(base_url + os.path.basename(hash_id_db_filename))

            def fetch_ok(data):
                hash_id_db_fd = open(hash_id_db_filename, "w")
                hash_id_db_fd.write(data)
                hash_id_db_fd.close()
                logging.info("Downloaded hash=>id database from %s" % url)

            def fetch_error(failure):
                exception = failure.value
                logging.warning("Couldn't download hash=>id database: %s" %
                                str(exception))

            result = fetch_async(url,
                                 cainfo=self._config.get("ssl_public_key"))
            result.addCallback(fetch_ok)
            result.addErrback(fetch_error)

            return result

        result = self._determine_hash_id_db_filename()
        result.addCallback(fetch_it)
        return result

    def _get_hash_id_db_base_url(self):

        base_url = self._config.get("package_hash_id_url")

        if not base_url:

            if not self._config.get("url"):
                # We really have no idea where to download from
                return None

            # If config.url is http://host:123/path/to/message-system
            # then we'll use http://host:123/path/to/hash-id-databases
            base_url = urlparse.urljoin(self._config.url.rstrip("/"),
                                        "hash-id-databases")

        return base_url.rstrip("/") + "/"

    def _apt_sources_have_changed(self):
        """Return a boolean indicating if the APT sources were modified."""
        from landscape.monitor.packagemonitor import PackageMonitor

        filenames = []

        if os.path.exists(self.sources_list_filename):
            filenames.append(self.sources_list_filename)

        if os.path.exists(self.sources_list_directory):
            filenames.extend(
                [os.path.join(self.sources_list_directory, filename) for
                 filename in os.listdir(self.sources_list_directory)])

        for filename in filenames:
            seconds_since_last_change = (
                time.time() - os.path.getmtime(filename))
            if seconds_since_last_change < PackageMonitor.run_interval:
                return True

        return False

    def run_smart_update(self):
        """Run smart-update and log a warning in case of non-zero exit code.

        @return: a deferred returning (out, err, code)
        """
        if self._config.force_smart_update or self._apt_sources_have_changed():
            args = ()
        else:
            args = ("--after", "%d" % self.smart_update_interval)
        result = getProcessOutputAndValue(self.smart_update_filename,
                                          args=args)

        def callback((out, err, code)):
            # smart-update --after N will exit with error code 1 when it
            # doesn't actually run the update code because to enough time
            # has passed yet, but we don't actually consider it a failure.
            smart_failed = False
            if code != 0 and code != 1:
                smart_failed = True
            if code == 1 and err.strip() != "":
                smart_failed = True
            if smart_failed:
                logging.warning("'%s' exited with status %d (%s)" % (
                    self.smart_update_filename, code, err))
            logging.debug("'%s' exited with status %d (out='%s', err='%s'" % (
                self.smart_update_filename, code, out, err))
            touch_file(self._config.smart_update_stamp_filename)
            return (out, err, code)

        result.addCallback(callback)
        return result

    def handle_task(self, task):
        message = task.data
        if message["type"] == "package-ids":
            return self._handle_package_ids(message)
        if message["type"] == "resynchronize":
            return self._handle_resynchronize()

    def _handle_package_ids(self, message):
        unknown_hashes = []

        try:
            request = self._store.get_hash_id_request(message["request-id"])
        except UnknownHashIDRequest:
            # We've lost this request somehow.  It will be re-requested later.
            return succeed(None)

        hash_ids = {}

        for hash, id in zip(request.hashes, message["ids"]):
            if id is None:
                unknown_hashes.append(hash)
            else:
                hash_ids[hash] = id

        self._store.set_hash_ids(hash_ids)

        logging.info("Received %d package hash => id translations, %d hashes "
                     "are unknown." % (len(hash_ids), len(unknown_hashes)))

        if unknown_hashes:
            result = self._handle_unknown_packages(unknown_hashes)
        else:
            result = succeed(None)

        # Remove the request if everything goes well.
        result.addCallback(lambda x: request.remove())

        return result

    def _handle_resynchronize(self):
        self._store.clear_available()
        self._store.clear_available_upgrades()
        self._store.clear_installed()
        self._store.clear_locked()
        self._store.clear_package_locks()

        # Don't clear the hash_id_requests table because the messages
        # associated with the existing requests might still have to be
        # delivered, and if we clear the table and later create a new request,
        # that new request could get the same id of one of the deleted ones,
        # and when the pending message eventually gets delivered the reporter
        # would think that the message is associated to the newly created
        # request, as it have the same id has the deleted request the message
        # actually refers to. This would cause the ids in the message to be
        # possibly mapped to the wrong hashes.
        #
        # This problem would happen for example when switching the client from
        # one Landscape server to another, because the uuid-changed event would
        # cause a resynchronize task to be created by the monitor. See #417122.

        return succeed(None)

    def _handle_unknown_packages(self, hashes):

        self._facade.ensure_channels_reloaded()

        hashes = set(hashes)
        added_hashes = []
        packages = []
        for package in self._facade.get_packages():
            hash = self._facade.get_package_hash(package)
            if hash in hashes:
                added_hashes.append(hash)
                skeleton = self._facade.get_package_skeleton(package)
                packages.append({"type": skeleton.type,
                                 "name": skeleton.name,
                                 "version": skeleton.version,
                                 "section": skeleton.section,
                                 "summary": skeleton.summary,
                                 "description": skeleton.description,
                                 "size": skeleton.size,
                                 "installed-size": skeleton.installed_size,
                                 "relations": skeleton.relations})

        if packages:
            logging.info("Queuing messages with data for %d packages to "
                         "exchange urgently." % len(packages))

            message = {"type": "add-packages", "packages": packages}

            result = self._send_message_with_hash_id_request(message,
                                                             added_hashes)
        else:
            result = succeed(None)

        return result

    def remove_expired_hash_id_requests(self):
        now = time.time()
        timeout = now - HASH_ID_REQUEST_TIMEOUT

        def update_or_remove(is_pending, request):
            if is_pending:
                # Request is still in the queue.  Update the timestamp.
                request.timestamp = now
            elif request.timestamp < timeout:
                # Request was delivered, and is older than the threshold.
                request.remove()

        results = []
        for request in self._store.iter_hash_id_requests():
            if request.message_id is None:
                # May happen in some rare cases, when a send_message() is
                # interrupted abruptly.  If it just fails normally, the
                # request is removed and so we don't get here.
                request.remove()
            else:
                result = self._broker.is_message_pending(request.message_id)
                result.addCallback(update_or_remove, request)
                results.append(result)

        return gather_results(results)

    def request_unknown_hashes(self):
        """Detect available packages for which we have no hash=>id mappings.

        This method will verify if there are packages that Smart knows
        about but for which we don't have an id yet (no hash => id
        translation), and deliver a message (unknown-package-hashes)
        to request them.

        Hashes previously requested won't be requested again, unless they
        have already expired and removed from the database.
        """
        self._facade.ensure_channels_reloaded()

        unknown_hashes = set()

        for package in self._facade.get_packages():
            hash = self._facade.get_package_hash(package)
            if self._store.get_hash_id(hash) is None:
                unknown_hashes.add(self._facade.get_package_hash(package))

        # Discard unknown hashes in existent requests.
        for request in self._store.iter_hash_id_requests():
            unknown_hashes -= set(request.hashes)

        if not unknown_hashes:
            result = succeed(None)
        else:
            unknown_hashes = sorted(unknown_hashes)
            unknown_hashes = unknown_hashes[:MAX_UNKNOWN_HASHES_PER_REQUEST]

            logging.info("Queuing request for package hash => id "
                         "translation on %d hash(es)." % len(unknown_hashes))

            message = {"type": "unknown-package-hashes",
                       "hashes": unknown_hashes}

            result = self._send_message_with_hash_id_request(message,
                                                             unknown_hashes)

        return result

    def _send_message_with_hash_id_request(self, message, unknown_hashes):
        """Create a hash_id_request and send message with "request-id"."""
        request = self._store.add_hash_id_request(unknown_hashes)
        message["request-id"] = request.id
        result = self._broker.send_message(message, True)

        def set_message_id(message_id):
            request.message_id = message_id

        def send_message_failed(failure):
            request.remove()
            return failure

        return result.addCallbacks(set_message_id, send_message_failed)

    def detect_changes(self):
        """Detect all changes concerning packages.

        If some changes were detected with respect to our last run, then an
        event of type 'package-data-changed' will be fired in the broker
        reactor.
        """

        def changes_detected(results):
            # Release all smart locks, in case the changer runs after us.
            self._facade.deinit()
            if True in results:
                # Something has changed, notify the broker.
                return self._broker.fire_event("package-data-changed")

        result = gather_results([self.detect_packages_changes(),
                                 self.detect_package_locks_changes()])
        return result.addCallback(changes_detected)

    def detect_packages_changes(self):
        """Detect changes in the universe of known packages.

        This method will verify if there are packages that:

        - are now installed, and were not;
        - are now available, and were not;
        - are now locked, and were not;
        - were previously available but are not anymore;
        - were previously installed but are not anymore;
        - were previously locked but are not anymore;

        Additionally it will report package locks that:

        - are now set, and were not;
        - were previously set but are not anymore;

        In all cases, the server is notified of the new situation
        with a "packages" message.

        @return: A deferred resulting in C{True} if package changes were
            detected with respect to the previous run, or C{False} otherwise.
        """
        self._facade.ensure_channels_reloaded()

        old_installed = set(self._store.get_installed())
        old_available = set(self._store.get_available())
        old_upgrades = set(self._store.get_available_upgrades())
        old_locked = set(self._store.get_locked())

        current_installed = set()
        current_available = set()
        current_upgrades = set()
        current_locked = set()

        for package in self._facade.get_packages():
            hash = self._facade.get_package_hash(package)
            id = self._store.get_hash_id(hash)
            if id is not None:
                if package.installed:
                    current_installed.add(id)
                    for loader in package.loaders:
                        # Is the package also in a non-installed
                        # loader?  IOW, "available".
                        if not loader.getInstalled():
                            current_available.add(id)
                            break
                else:
                    current_available.add(id)

                # Are there any packages that this package is an upgrade for?
                for upgrade in package.upgrades:
                    for provides in upgrade.providedby:
                        for provides_package in provides.packages:
                            if provides_package.installed:
                                current_upgrades.add(id)
                                break
                        else:
                            continue
                        break
                    else:
                        continue
                    break

        for package in self._facade.get_locked_packages():
            hash = self._facade.get_package_hash(package)
            id = self._store.get_hash_id(hash)
            if id is not None:
                current_locked.add(id)

        new_installed = current_installed - old_installed
        new_available = current_available - old_available
        new_upgrades = current_upgrades - old_upgrades
        new_locked = current_locked - old_locked

        not_installed = old_installed - current_installed
        not_available = old_available - current_available
        not_upgrades = old_upgrades - current_upgrades
        not_locked = old_locked - current_locked

        message = {}
        if new_installed:
            message["installed"] = \
                list(sequence_to_ranges(sorted(new_installed)))
        if new_available:
            message["available"] = \
                list(sequence_to_ranges(sorted(new_available)))
        if new_upgrades:
            message["available-upgrades"] = \
                list(sequence_to_ranges(sorted(new_upgrades)))
        if new_locked:
            message["locked"] = \
                list(sequence_to_ranges(sorted(new_locked)))

        if not_installed:
            message["not-installed"] = \
                list(sequence_to_ranges(sorted(not_installed)))
        if not_available:
            message["not-available"] = \
                list(sequence_to_ranges(sorted(not_available)))
        if not_upgrades:
            message["not-available-upgrades"] = \
                list(sequence_to_ranges(sorted(not_upgrades)))
        if not_locked:
            message["not-locked"] = \
                list(sequence_to_ranges(sorted(not_locked)))

        if not message:
            return succeed(False)

        message["type"] = "packages"
        result = self._broker.send_message(message, True)

        logging.info("Queuing message with changes in known packages: "
                     "%d installed, %d available, %d available upgrades, "
                     "%d locked, %d not installed, %d not available, "
                     "%d not available upgrades, %d not locked."
                     % (len(new_installed), len(new_available),
                        len(new_upgrades), len(new_locked),
                        len(not_installed), len(not_available),
                        len(not_upgrades), len(not_locked)))

        def update_currently_known(result):
            if new_installed:
                self._store.add_installed(new_installed)
            if not_installed:
                self._store.remove_installed(not_installed)
            if new_available:
                self._store.add_available(new_available)
            if new_locked:
                self._store.add_locked(new_locked)
            if not_available:
                self._store.remove_available(not_available)
            if new_upgrades:
                self._store.add_available_upgrades(new_upgrades)
            if not_upgrades:
                self._store.remove_available_upgrades(not_upgrades)
            if not_locked:
                self._store.remove_locked(not_locked)
            # Something has changed wrt the former run, let's return True
            return True

        result.addCallback(update_currently_known)

        return result

    def detect_package_locks_changes(self):
        """Detect changes in known package locks.

        This method will verify if there are package locks that:

        - are now set, and were not;
        - were previously set but are not anymore;

        In all cases, the server is notified of the new situation
        with a "packages" message.

        @return: A deferred resulting in C{True} if package lock changes were
            detected with respect to the previous run, or C{False} otherwise.
        """
        old_package_locks = set(self._store.get_package_locks())
        current_package_locks = set(self._facade.get_package_locks())

        set_package_locks = current_package_locks - old_package_locks
        unset_package_locks = old_package_locks - current_package_locks

        message = {}
        if set_package_locks:
            message["created"] = sorted(set_package_locks)
        if unset_package_locks:
            message["deleted"] = sorted(unset_package_locks)

        if not message:
            return succeed(False)

        message["type"] = "package-locks"
        result = self._broker.send_message(message, True)

        logging.info("Queuing message with changes in known package locks:"
                     " %d created, %d deleted." %
                     (len(set_package_locks), len(unset_package_locks)))

        def update_currently_known(result):
            if set_package_locks:
                self._store.add_package_locks(set_package_locks)
            if unset_package_locks:
                self._store.remove_package_locks(unset_package_locks)
            return True

        result.addCallback(update_currently_known)

        return result


def main(args):
    return run_task_handler(PackageReporter, args)


def find_reporter_command():
    dirname = os.path.dirname(os.path.abspath(sys.argv[0]))
    return os.path.join(dirname, "landscape-package-reporter")
