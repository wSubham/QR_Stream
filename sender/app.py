"""
QRStream Sender
Main Entry Point
"""

from pathlib import Path

from sender.file_reader import FileReader
from sender.file_splitter import FileSplitter
from sender.session import TransferSession
from sender.transmission_engine import TransmissionEngine
from sender.file_reader import FileReader

# ROOT = Path(__file__).resolve().parent

def main():

    # ============================================
    # Select File
    # ============================================

    from pathlib import Path

    ROOT = Path(__file__).resolve().parent.parent

    filepath = ROOT / "samples" / "sample4.jpeg"
    filepath = str(Path(filepath))

    print("=" * 60)
    print("QRStream Sender")
    print("=" * 60)

    # ============================================
    # Read File
    # ============================================

    file_data = FileReader.read(filepath)

    # ============================================
    # Split File
    # ============================================

    chunks = list(
        FileSplitter.stream(file_data)
    )

    # ============================================
    # Create Transfer Session
    # ============================================

    session = TransferSession.create(

        filepath=filepath,

        total_chunks=len(chunks)

    )

    # ============================================
    # Start Transmission
    # ============================================

    engine = TransmissionEngine(

        session=session,

        chunks=chunks

    )

    engine.start()


if __name__ == "__main__":

    main()