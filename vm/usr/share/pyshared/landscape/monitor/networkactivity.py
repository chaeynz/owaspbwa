"""
A monitor that collects data on network activity, and sends messages
with the inbound/outbound traffic per interface per step interval.
"""

import time

from landscape.lib.network import get_network_traffic, is_64
from landscape.accumulate import Accumulator

from landscape.monitor.plugin import MonitorPlugin


class NetworkActivity(MonitorPlugin):
    """
    Collect data regarding a machine's network activity.
    """

    message_type = "network-activity"
    persist_name = message_type
    run_interval = 30

    def __init__(self, network_activity_file="/proc/net/dev",
                 create_time=time.time):
        self._source_file = network_activity_file
        # accumulated values for sending out via message
        self._network_activity = {}
        # our last traffic sample for calculating a traffic delta
        self._last_activity = {}
        self._create_time = create_time
        # We don't rollover on 64 bits, as 16 exabytes is a lot.
        self._rollover_maxint = 0 if is_64() else pow(2, 32)

    def register(self, registry):
        super(NetworkActivity, self).register(registry)
        self._accumulate = Accumulator(self._persist, self.registry.step_size)
        self.call_on_accepted("network-activity", self.exchange, True)

    def create_message(self):
        network_activity = self._network_activity
        if not network_activity:
            return
        self._network_activity = {}
        return {"type": "network-activity", "activities": network_activity}

    def send_message(self, urgent):
        message = self.create_message()
        if not message:
            return
        self.registry.broker.send_message(message, urgent=urgent)

    def exchange(self, urgent=False):
        self.registry.broker.call_if_accepted("network-activity",
                                              self.send_message, urgent)

    def _traffic_delta(self, new_traffic):
        """
        Given network activity metrics across all interfaces, calculate
        and return the delta data transferred for inbound and outbound
        traffic. Returns a tuple of interface name, outbound delta,
        inbound delta.
        """
        for interface in new_traffic:
            traffic = new_traffic[interface]
            if interface in self._last_activity:
                previous_out, previous_in = self._last_activity[interface]
                delta_out = traffic["send_bytes"] - previous_out
                delta_in = traffic["recv_bytes"] - previous_in
                if delta_out < 0:
                    delta_out += self._rollover_maxint
                if delta_in < 0:
                    delta_in += self._rollover_maxint
                # If it's still zero or less, we discard the value. The next
                # value will be compared to the current traffic, and
                # hopefully things will catch up.
                if delta_out <= 0 and delta_in <= 0:
                    continue

                yield interface, delta_out, delta_in
            self._last_activity[interface] = (
                traffic["send_bytes"], traffic["recv_bytes"])
        for interface in self._last_activity.keys():
            if interface not in new_traffic:
                del self._last_activity[interface]

    def run(self):
        """
        Sample network traffic statistics and store them into the
        accumulator, recording step data.
        """
        new_timestamp = int(self._create_time())
        new_traffic = get_network_traffic(self._source_file)
        for interface, delta_out, delta_in in self._traffic_delta(new_traffic):
            out_step_data = self._accumulate(
                new_timestamp, delta_out, "delta-out-%s" % interface)

            in_step_data = self._accumulate(
                new_timestamp, delta_in, "delta-in-%s" % interface)

            # there's only data when we cross a step boundary
            if not (in_step_data and out_step_data):
                continue

            steps = self._network_activity.setdefault(interface, [])
            steps.append(
                (in_step_data[0], int(in_step_data[1]), int(out_step_data[1])))
