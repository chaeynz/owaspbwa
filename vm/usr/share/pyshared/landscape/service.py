import os
import logging
import signal

from twisted.application.service import Application, Service
from twisted.application.app import startApplication

from landscape.log import rotate_logs
from landscape.reactor import TwistedReactor
from landscape.deployment import get_versioned_persist, init_logging
from landscape.lib import bpickle_dbus


class LandscapeService(Service, object):
    """Utility superclass for defining Landscape services.

    This sets up the reactor and L{Persist} object.

    @cvar service_name: The lower-case name of the service. This is used to
        generate the bpickle and the Unix socket filenames.
    @ivar config: A L{Configuration} object.
    @ivar reactor: A L{TwistedReactor} object.
    @ivar persist: A L{Persist} object, if C{persist_filename} is defined.
    @ivar factory: A L{LandscapeComponentProtocolFactory}, it must be provided
        by instances of sub-classes.
    """
    reactor_factory = TwistedReactor
    persist_filename = None

    def __init__(self, config):
        self.config = config
        bpickle_dbus.install()
        self.reactor = self.reactor_factory()
        if self.persist_filename:
            self.persist = get_versioned_persist(self)
        if not (self.config is not None and self.config.ignore_sigusr1):
            signal.signal(signal.SIGUSR1, lambda signal, frame: rotate_logs())
        self.socket = os.path.join(self.config.sockets_path,
                                   self.service_name + ".sock")

    def startService(self):
        Service.startService(self)
        logging.info("%s started with config %s" % (
            self.service_name.capitalize(), self.config.get_config_filename()))
        self.port = self.reactor.listen_unix(self.socket, self.factory)

    def stopService(self):
        # We don't need to call port.stopListening(), because the reactor
        # shutdown sequence will do that for us.
        Service.stopService(self)
        logging.info("%s stopped with config %s" % (
            self.service_name.capitalize(), self.config.get_config_filename()))


def run_landscape_service(configuration_class, service_class, args):
    """Run a Landscape service.

    This function instantiates the specified L{LandscapeService} subclass and
    attaches the resulting service object to a Twisted C{Application}.  After
    that it starts the Twisted L{Application} and calls the
    L{TwistedReactor.run} method of the L{LandscapeService}'s reactor.

    @param configuration_class: The service-specific subclass of
        L{Configuration} used to parse C{args} and build the C{service_class}
        object.
    @param service_class: The L{LandscapeService} subclass to create and start.
    @param args: Command line arguments used to initialize the configuration.
    """
    # Let's consider adding this:
    # from twisted.python.log import (
    #     startLoggingWithObserver, PythonLoggingObserver)
    # startLoggingWithObserver(PythonLoggingObserver().emit, setStdout=False)

    configuration = configuration_class()
    configuration.load(args)
    init_logging(configuration, service_class.service_name)
    application = Application("landscape-%s" % (service_class.service_name,))
    service = service_class(configuration)
    service.setServiceParent(application)
    startApplication(application, False)

    if configuration.ignore_sigint:
        signal.signal(signal.SIGINT, signal.SIG_IGN)

    service.reactor.run()
