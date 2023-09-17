"""
Network introspection utilities using ioctl and the /proc filesystem.
"""
import array
import fcntl
import socket
import struct

__all__ = ["get_active_device_info", "get_network_traffic", "is_64"]

# from header /usr/include/bits/ioctls.h
SIOCGIFCONF = 0x8912
SIOCGIFFLAGS = 0x8913
SIOCGIFNETMASK = 0x891b
SIOCGIFBRDADDR = 0x8919
SIOCGIFADDR = 0x8915
SIOCGIFHWADDR = 0x8927


# struct definition from header /usr/include/net/if.h
# the struct size varies according to the platform arch size
# a minimal c program was used to determine the size of the
# struct, standard headers removed for brevity.
"""
#include <linux/if.h>
int main() {
  printf("Size of struct %lu\n", sizeof(struct ifreq));
}
"""

IF_STRUCT_SIZE_32 = 32
IF_STRUCT_SIZE_64 = 40


def is_64():
    """Returns C{True} if the platform is 64-bit, otherwise C{False}."""
    return struct.calcsize("l") == 8


# initialize the struct size as per the machine's archictecture
IF_STRUCT_SIZE = is_64() and IF_STRUCT_SIZE_64 or IF_STRUCT_SIZE_32


def get_active_interfaces(sock):
    """Generator yields active network interface names.

    @param sock: a socket instance.
    """
    max_interfaces = 128

    # Setup an an array to hold our response, and initialized to null strings.
    interfaces = array.array("B", "\0" * max_interfaces * IF_STRUCT_SIZE)
    buffer_size = interfaces.buffer_info()[0]
    packed_bytes = struct.pack(
        "iL", max_interfaces * IF_STRUCT_SIZE, buffer_size)

    byte_length = struct.unpack(
        "iL", fcntl.ioctl(sock.fileno(), SIOCGIFCONF, packed_bytes))[0]

    result = interfaces.tostring()

    # Generator over the interface names
    already_found = set()
    for index in range(0, byte_length, IF_STRUCT_SIZE):
        ifreq_struct = result[index:index+IF_STRUCT_SIZE]
        interface_name = ifreq_struct[:ifreq_struct.index("\0")]
        if interface_name not in already_found:
            already_found.add(interface_name)
            yield interface_name


def get_broadcast_address(sock, interface):
    """Return the broadcast address associated to an interface.

    @param sock: a socket instance.
    @param interface: The name of the interface.
    """
    return socket.inet_ntoa(fcntl.ioctl(
        sock.fileno(),
        SIOCGIFBRDADDR,
        struct.pack("256s", interface[:15]))[20:24])


def get_netmask(sock, interface):
    """Return the network mask associated to an interface.

    @param sock: a socket instance.
    @param interface: The name of the interface.
    """
    return socket.inet_ntoa(fcntl.ioctl(
        sock.fileno(),
        SIOCGIFNETMASK,
        struct.pack("256s", interface[:15]))[20:24])


def get_ip_address(sock, interface):
    """Return the IP address associated to the interface.

    @param sock: a socket instance.
    @param interface: The name of the interface.
    """
    return socket.inet_ntoa(fcntl.ioctl(
        sock.fileno(),
        SIOCGIFADDR,
        struct.pack("256s", interface[:15]))[20:24])


def get_mac_address(sock, interface):
    """
    Return the hardware MAC address for an interface in human friendly form,
    ie. six colon separated groups of two hexadecimal digits.

    @param sock: a socket instance.
    @param interface: The name of the interface.
    """
    mac_address = fcntl.ioctl(
        sock.fileno(), SIOCGIFHWADDR, struct.pack("256s", interface[:15]))
    return "".join(["%02x:" % ord(char) for char in mac_address[18:24]])[:-1]


def get_flags(sock, interface):
    """Return the integer value of the interface flags for the given interface.

    @param sock: a socket instance.
    @param interface: The name of the interface.
    @see /usr/include/linux/if.h for the meaning of the flags.
    """
    data = fcntl.ioctl(
        sock.fileno(), SIOCGIFFLAGS, struct.pack("256s", interface[:15]))
    return struct.unpack("H", data[16:18])[0]


def get_active_device_info(skipped_interfaces=("lo",)):
    """
    Returns a dictionary containing information on each active network
    interface present on a machine.
    """
    results = []
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM, socket.IPPROTO_IP)
    for interface in get_active_interfaces(sock):
        if interface in skipped_interfaces:
            continue
        interface_info = {"interface": interface}
        interface_info["ip_address"] = get_ip_address(sock, interface)
        interface_info["mac_address"] = get_mac_address(sock, interface)
        interface_info["broadcast_address"] = get_broadcast_address(sock,
                                                                    interface)
        interface_info["netmask"] = get_netmask(sock, interface)
        interface_info["flags"] = get_flags(sock, interface)
        results.append(interface_info)
    del sock
    return results


def get_network_traffic(source_file="/proc/net/dev"):
    """
    Retrieves an array of information regarding the network activity per
    network interface.
    """
    netdev = open(source_file, "r")
    lines = netdev.readlines()
    netdev.close()

    # Parse out the column headers as keys.
    _, receive_columns, transmit_columns = lines[1].split("|")
    columns = ["recv_%s" % column for column in receive_columns.split()]
    columns.extend(["send_%s" % column for column in transmit_columns.split()])

    # Parse out the network devices.
    devices = {}
    for line in lines[2:]:
        if not ":" in line:
            continue
        device, data = line.split(":")
        device = device.strip()
        devices[device] = dict(zip(columns, map(long, data.split())))
    return devices


if __name__ == "__main__":
    import pprint
    pprint.pprint(get_active_device_info())
