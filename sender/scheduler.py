"""
QRStream Packet Scheduler

Responsible for deciding which packet is sent next.

Sequence:

START x N
↓

DATA 0
↓

DATA 1
↓

...

↓

DATA N

↓

END x N
"""

from enum import Enum

from sender.config import START_REPEAT, END_REPEAT
from sender.packet_builder import PacketBuilder


class SchedulerState(Enum):
    START = 1
    DATA = 2
    END = 3
    FINISHED = 4


class PacketScheduler:

    def __init__(
        self,
        session,
        chunks,
    ):

        self.session = session

        self.chunks = chunks

        self.state = SchedulerState.START

        self.start_counter = 0

        self.end_counter = 0

        self.chunk_index = 0

    # ------------------------------

    def has_next(self):

        return self.state != SchedulerState.FINISHED

    # ------------------------------

    def next_packet(self):

        if self.state == SchedulerState.START:
            return self._start_packet()

        if self.state == SchedulerState.DATA:
            return self._data_packet()

        if self.state == SchedulerState.END:
            return self._end_packet()

        return None

    # ------------------------------

    def _start_packet(self):

        packet = PacketBuilder.start(self.session)

        self.start_counter += 1

        if self.start_counter >= START_REPEAT:
            self.state = SchedulerState.DATA

        return packet

    # ------------------------------

    def _data_packet(self):

        chunk_number, total_chunks, payload = self.chunks[
            self.chunk_index
        ]

        packet = PacketBuilder.data(
            self.session,
            chunk_number,
            payload,
        )

        self.chunk_index += 1

        self.session.current_chunk = self.chunk_index

        if self.chunk_index >= len(self.chunks):
            self.state = SchedulerState.END

        return packet

    # ------------------------------

    def _end_packet(self):

        packet = PacketBuilder.end(
            self.session
        )

        self.end_counter += 1

        if self.end_counter >= END_REPEAT:

            self.state = SchedulerState.FINISHED

            self.session.completed = True

        return packet