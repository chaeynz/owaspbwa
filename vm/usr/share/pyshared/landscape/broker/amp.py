from twisted.internet.defer import maybeDeferred, execute, succeed

from landscape.lib.amp import RemoteObject
from landscape.amp import (
    ComponentProtocol, ComponentProtocolFactory, RemoteComponentConnector,
    RemoteComponentsRegistry)
from landscape.broker.server import BrokerServer
from landscape.broker.client import BrokerClient
from landscape.monitor.monitor import Monitor
from landscape.manager.manager import Manager


class BrokerServerProtocol(ComponentProtocol):
    """
    Communication protocol between the broker server and its clients.
    """
    methods = (ComponentProtocol.methods +
               ["fire_event",
                "get_accepted_message_types",
                "get_server_uuid",
                "is_message_pending",
                "register",
                "register_client",
                "register_client_accepted_message_type",
                "reload_configuration",
                "send_message",
                "stop_clients",
                "listen_events"])


class BrokerServerProtocolFactory(ComponentProtocolFactory):

    protocol = BrokerServerProtocol


class RemoteBroker(RemoteObject):

    def call_if_accepted(self, type, callable, *args):
        """Call C{callable} if C{type} is an accepted message type."""
        deferred_types = self.get_accepted_message_types()

        def got_accepted_types(result):
            if type in result:
                return callable(*args)
        deferred_types.addCallback(got_accepted_types)
        return deferred_types

    def call_on_event(self, handlers):
        """Call a given handler as soon as a certain event occurs.

        @param handlers: A dictionary mapping event types to callables, where
            an event type is string (the name of the event). When the first of
            the given event types occurs in the broker reactor, the associated
            callable will be fired.
        """
        result = self.listen_events(handlers.keys())
        return result.addCallback(lambda event_type: handlers[event_type]())


class FakeRemoteBroker(object):
    """Looks like L{RemoteBroker}, but actually talks to local objects."""

    def __init__(self, exchanger, message_store):
        self.exchanger = exchanger
        self.message_store = message_store
        self.protocol = BrokerServerProtocol()

    def ping(self):
        return succeed(True)

    def call_if_accepted(self, type, callable, *args):
        if type in self.message_store.get_accepted_types():
            return maybeDeferred(callable, *args)
        return succeed(None)

    def send_message(self, message, urgent=False):
        """Send to the previously given L{MessageExchange} object."""

        # Check that the message to be sent is serializable over AMP
        from landscape.lib.amp import MethodCallArgument
        assert(MethodCallArgument.check(message))

        return execute(self.exchanger.send, message, urgent=urgent)

    def register_client_accepted_message_type(self, type):
        return execute(self.exchanger.register_client_accepted_message_type,
                       type)

    def get_accepted_message_types(self):
        """
        Return a deferred resulting in the list of message types accepted
        by the Landscape server.
        """
        return execute(self.message_store.get_accepted_types)


class BrokerClientProtocol(ComponentProtocol):
    """Communication protocol between a client and the broker."""

    methods = (ComponentProtocol.methods + ["fire_event", "message"])


class BrokerClientProtocolFactory(ComponentProtocolFactory):

    protocol = BrokerClientProtocol


class RemoteClient(RemoteObject):
    """A remote L{BrokerClient} connected to a L{BrokerServer}."""


class RemoteBrokerConnector(RemoteComponentConnector):
    """Helper to create connections with the L{BrokerServer}."""

    factory = BrokerClientProtocolFactory
    remote = RemoteBroker
    component = BrokerServer


class RemoteClientConnector(RemoteComponentConnector):
    """Helper to create connections with the L{BrokerServer}."""

    factory = BrokerServerProtocolFactory
    remote = RemoteClient
    component = BrokerClient


class RemoteMonitorConnector(RemoteClientConnector):
    """Helper to create connections with the L{Monitor}."""

    component = Monitor


class RemoteManagerConnector(RemoteClientConnector):
    """Helper for creating connections with the L{Monitor}."""

    component = Manager

RemoteComponentsRegistry.register(RemoteBrokerConnector)
RemoteComponentsRegistry.register(RemoteClientConnector)
RemoteComponentsRegistry.register(RemoteMonitorConnector)
RemoteComponentsRegistry.register(RemoteManagerConnector)
