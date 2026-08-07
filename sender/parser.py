"""
QRStream Packet Parser
"""

import struct
import uuid

from sender.protocol import (
    PacketType,
    QRPacket,
)

HEADER_FORMAT = ">4sBB16sIIHI"

HEADER_SIZE = struct.calcsize(HEADER_FORMAT)


class PacketParser:

    @staticmethod
    def parse(packet_bytes: bytes):

        header = packet_bytes[:HEADER_SIZE]

        payload = packet_bytes[HEADER_SIZE:]

        (
            magic,
            version,
            packet_type,
            transfer_id,
            chunk_number,
            total_chunks,
            payload_size,
            crc32,
        ) = struct.unpack(
            HEADER_FORMAT,
            header,
        )

        if magic != b"QRTP":
            raise ValueError("Invalid protocol")

        return QRPacket(

            packet_type=PacketType(packet_type),

            version=version,

            transfer_id=uuid.UUID(bytes=transfer_id),

            chunk_number=chunk_number,

            total_chunks=total_chunks,

            payload=payload[:payload_size],

            crc32=crc32,
        )