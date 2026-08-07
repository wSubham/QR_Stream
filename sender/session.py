"""
Transfer Session
"""

from dataclasses import dataclass
from pathlib import Path
from uuid import UUID, uuid4


@dataclass(slots=True)
class TransferSession:

    transfer_id: UUID

    filename: str

    filesize: int

    total_chunks: int

    current_chunk: int = 0

    completed: bool = False

    @classmethod
    def create(cls, filepath: str, total_chunks: int):

        p = Path(filepath)

        return cls(
            transfer_id=uuid4(),
            filename=p.name,
            filesize=p.stat().st_size,
            total_chunks=total_chunks,
        )