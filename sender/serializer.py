"""
Packet Serializer
"""

import struct

from sender.protocol import (
    PROTOCOL_MAGIC,
    QRPacket,
)

HEADER_FORMAT = ">4sBB16sIIHI"

HEADER_SIZE = struct.calcsize(HEADER_FORMAT)


class PacketSerializer:

    @staticmethod
    def serialize(packet: QRPacket):

        header = struct.pack(

            HEADER_FORMAT,

            PROTOCOL_MAGIC,

            packet.version,

            packet.packet_type.value,

            packet.transfer_id.bytes,

            packet.chunk_number,

            packet.total_chunks,

            len(packet.payload),

            packet.crc32,
        )

        return header + packet.payload

    @staticmethod
    def deserialize(data: bytes):

        header = data[:HEADER_SIZE]

        payload = data[HEADER_SIZE:]

        return struct.unpack(
            HEADER_FORMAT,
            header,
        ), payload