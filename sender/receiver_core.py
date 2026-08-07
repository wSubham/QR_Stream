"""
QRStream Receiver Core

Simulates the Android receiver.

Responsibilities:
- Read packets
- Verify CRC
- Store chunks
- Reconstruct file
"""

import json
import os
import zlib

from sender.protocol import PacketType
from sender.parser import PacketParser


class ReceiverCore:

    def __init__(self):

        self.metadata = None

        self.chunks = {}

        self.total_chunks = 0

        self.transfer_finished = False

    # -------------------------------------------------

    def receive(self, packet_bytes: bytes):

        packet = PacketParser.parse(packet_bytes)

        # CRC Verification
        crc = zlib.crc32(packet.payload)

        if crc != packet.crc32:
            raise ValueError("CRC Check Failed")

        # START
        if packet.packet_type == PacketType.START:

            self.metadata = json.loads(
                packet.payload.decode("utf-8")
            )

            self.total_chunks = self.metadata["total_chunks"]

            print("=" * 50)
            print("TRANSFER STARTED")
            print("=" * 50)

            for key, value in self.metadata.items():
                print(f"{key:15}: {value}")

            print("=" * 50)

            return

        # DATA
        if packet.packet_type == PacketType.DATA:

            self.chunks[packet.chunk_number] = packet.payload

            print(
                f"Received Chunk "
                f"{packet.chunk_number + 1}/"
                f"{self.total_chunks}"
            )

            return

        # END
        if packet.packet_type == PacketType.END:

            print("\nEND Packet Received")

            self.transfer_finished = True

    # -------------------------------------------------

    def completed(self):

        return (
            self.transfer_finished
            and
            len(self.chunks) == self.total_chunks
        )

    # -------------------------------------------------

    def missing_chunks(self):

        missing = []

        for i in range(self.total_chunks):

            if i not in self.chunks:

                missing.append(i)

        return missing

    # -------------------------------------------------

    def save(self, directory="received"):

        if not self.completed():

            raise RuntimeError(
                "Transfer incomplete"
            )

        os.makedirs(
            directory,
            exist_ok=True
        )

        path = os.path.join(
            directory,
            self.metadata["filename"]
        )

        with open(path, "wb") as f:

            for i in range(self.total_chunks):

                f.write(self.chunks[i])

        print("\nSaved:", path)

        return path