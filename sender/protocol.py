"""
QRStream Protocol Definitions
"""

from dataclasses import dataclass
from enum import Enum
from uuid import UUID


PROTOCOL_MAGIC = b"QRTP"

PROTOCOL_VERSION = 1


class PacketType(Enum):
    START = 1
    DATA = 2
    END = 3


@dataclass(slots=True)
class QRPacket:

    packet_type: PacketType

    version: int

    transfer_id: UUID

    chunk_number: int

    total_chunks: int

    payload: bytes

    crc32: int