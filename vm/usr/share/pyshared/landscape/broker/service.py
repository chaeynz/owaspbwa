"""Deployment code for the monitor."""

import os
from landscape.lib.fetch import fetch_async
from landscape.service import LandscapeService, run_landscape_service
from landscape.broker.registration import RegistrationHandler, Identity
from landscape.broker.config import BrokerConfiguration
from landscape.broker.transport import HTTPTransport
from landscape.broker.exchange import MessageExchange
from landscape.broker.exchangestore import ExchangeStore
from landscape.broker.ping import Pinger
from landscape.broker.store import get_default_message_store
from landscape.broker.server import BrokerServer
from landscape.broker.amp import BrokerServerProtocolFactory


class BrokerService(LandscapeService):
    """The core C{Service} of the Landscape Broker C{Application}.

    The Landscape broker service handles all the communication between the
    client and server.  When started it creates and runs all necessary
    components to exchange messages with the Landscape server.

    @cvar service_name: C{broker}

    @ivar persist_filename: Path to broker-specific persistent data.
    @ivar persist: A L{Persist} object saving and loading data from
        C{self.persist_filename}.
    @ivar message_store: A L{MessageStore} used by the C{exchanger} to
        queue outgoing messages.
    @ivar transport: An L{HTTPTransport} used by the C{exchanger} to deliver
        messages.
    @ivar identity: The L{Identity} of the Landscape client the broker runs on.
    @ivar exchanger: The L{MessageExchange} exchanges messages with the server.
    @ivar pinger: The L{Pinger} checks if the server has new messages for us.
    @ivar registration: The L{RegistrationHandler} performs the initial
        registration.
    """

    transport_factory = HTTPTransport
    pinger_factory = Pinger
    service_name = BrokerServer.name

    def __init__(self, config):
        """
        @param config: A L{BrokerConfiguration}.
        """
        self.persist_filename = os.path.join(
            config.data_path, "%s.bpickle" % (self.service_name,))
        super(BrokerService, self).__init__(config)
        self.transport = self.transport_factory(config.url,
                                                config.ssl_public_key)
        self.message_store = get_default_message_store(
            self.persist, config.message_store_path)
        self.identity = Identity(self.config, self.persist)
        exchange_store = ExchangeStore(self.config.exchange_store_path)
        self.exchanger = MessageExchange(
            self.reactor, self.message_store, self.transport, self.identity,
            exchange_store, config.exchange_interval,
            config.urgent_exchange_interval)
        self.pinger = self.pinger_factory(self.reactor, config.ping_url,
                                          self.identity, self.exchanger)
        self.registration = RegistrationHandler(
            config, self.identity, self.reactor, self.exchanger, self.pinger,
            self.message_store, fetch_async)
        self.reactor.call_on("post-exit", self._exit)
        self.broker = BrokerServer(self.config, self.reactor, self.exchanger,
                                   self.registration, self.message_store)
        self.factory = BrokerServerProtocolFactory(object=self.broker)

    def _exit(self):
        # Our reactor calls the Twisted reactor's crash() method rather
        # than the real stop.  As a consequence, if we use it here, normal
        # termination doesn't happen, and stopService() would never get
        # called.
        from twisted.internet import reactor
        reactor.stop()

    def startService(self):
        """Start the broker.

        Create a L{BrokerServer} listening on C{broker_socket_path} for clients
        connecting with the L{BrokerClientProtocol}, and start the
        L{MessageExchange} and L{Pinger} services.
        """
        super(BrokerService, self).startService()
        self.exchanger.start()
        self.pinger.start()

    def stopService(self):
        """Stop the broker."""
        self.exchanger.stop()
        super(BrokerService, self).stopService()


def run(args):
    """Run the application, given some command line arguments."""
    run_landscape_service(BrokerConfiguration, BrokerService, args)
