"""
Chunk Generator
"""

from sender.config import CHUNK_SIZE


class FileSplitter:

    @staticmethod
    def stream(data: bytes):

        total = (len(data) + CHUNK_SIZE - 1) // CHUNK_SIZE

        for index in range(total):

            start = index * CHUNK_SIZE

            end = start + CHUNK_SIZE

            yield (
                index,
                total,
                data[start:end],
            )