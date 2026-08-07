"""
QRStream Transmission Engine

Responsible for:
- Creating packets from the scheduler
- Serializing packets
- Generating QR codes
- Displaying QR codes
"""

from sender.scheduler import PacketScheduler
from sender.serializer import PacketSerializer
from sender.qr_generator import QRGenerator
from sender.qr_display import QRDisplay


class TransmissionEngine:

    def __init__(self, session, chunks):

        self.session = session

        self.scheduler = PacketScheduler(
            session,
            chunks
        )

        self.generator = QRGenerator()

        self.display = QRDisplay()

    # ----------------------------------

    def start(self):

        print("=" * 60)
        print("QRStream Sender Started")
        print("=" * 60)
        print(f"File           : {self.session.filename}")
        print(f"Size           : {self.session.filesize} Bytes")
        print(f"Total Chunks   : {self.session.total_chunks}")
        print("=" * 60)

        running = True

        while running and self.scheduler.has_next():

            packet = self.scheduler.next_packet()

            raw_packet = PacketSerializer.serialize(packet)

            frame = self.generator.generate(raw_packet)

            running = self.display.show(
                frame=frame,
                title=f"{packet.packet_type.name}",
                chunk=packet.chunk_number,
                total=self.session.total_chunks,
            )

        self.display.close()

        print("\nTransmission Finished")

        return True