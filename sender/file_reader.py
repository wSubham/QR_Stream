"""
Reads files as binary
"""

from pathlib import Path


class FileReader:

    @staticmethod
    def read(path: str):

        file = Path(path)

        if not file.exists():
            raise FileNotFoundError(path)

        with open(file, "rb") as f:
            return f.read()

    @staticmethod
    def size(path: str):

        return Path(path).stat().st_size