"""
QRStream Packet Builder
"""

import json
import mimetypes
import zlib

from sender.config import CHUNK_SIZE
from sender.protocol import (
    PacketType,
    PROTOCOL_VERSION,
    QRPacket,
)

from sender.session import TransferSession


class PacketBuilder:

    @staticmethod
    def start(session: TransferSession):

        metadata = {

            "protocol": "QRStream",

            "version": PROTOCOL_VERSION,

            "transfer_id": str(session.transfer_id),

            "filename": session.filename,

            "filesize": session.filesize,

            "chunk_size": CHUNK_SIZE,

            "total_chunks": session.total_chunks,

            "mime_type": mimetypes.guess_type(session.filename)[0]
            or "application/octet-stream",
        }

        payload = json.dumps(
            metadata,
            separators=(",", ":"),
        ).encode("utf-8")

        return QRPacket(

            packet_type=PacketType.START,

            version=PROTOCOL_VERSION,

            transfer_id=session.transfer_id,

            chunk_number=0,

            total_chunks=session.total_chunks,

            payload=payload,

            crc32=zlib.crc32(payload),
        )

    @staticmethod
    def data(
        session: TransferSession,
        chunk_number: int,
        payload: bytes,
    ):

        return QRPacket(

            packet_type=PacketType.DATA,

            version=PROTOCOL_VERSION,

            transfer_id=session.transfer_id,

            chunk_number=chunk_number,

            total_chunks=session.total_chunks,

            payload=payload,

            crc32=zlib.crc32(payload),
        )

    @staticmethod
    def end(session: TransferSession):

        payload = json.dumps(
            {
                "status": "END"
            }
        ).encode()

        return QRPacket(

            packet_type=PacketType.END,

            version=PROTOCOL_VERSION,

            transfer_id=session.transfer_id,

            chunk_number=session.total_chunks,

            total_chunks=session.total_chunks,

            payload=payload,

            crc32=zlib.crc32(payload),
        )