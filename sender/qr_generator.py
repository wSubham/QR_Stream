"""
QR Generator

Converts serialized packet bytes into OpenCV QR images.
"""

import base64
import io

import cv2
import numpy as np
import segno
from PIL import Image

from sender.config import (
    QR_BORDER,
    QR_ERROR,
    QR_SCALE,
)


class QRGenerator:

    def __init__(self):

        # Final QR size shown in sender window
        self.output_size = 420

    # --------------------------------------------------------

    def generate(self, packet_bytes: bytes):

        # Convert binary packet into Base64 text
        qr_text = base64.b64encode(packet_bytes).decode("ascii")

        # Generate QR
        qr = segno.make(
            qr_text,
            error=QR_ERROR,
            micro=False,
        )

        # Save QR into memory
        buffer = io.BytesIO()

        qr.save(
            buffer,
            kind="png",
            scale=QR_SCALE,
            border=QR_BORDER,
        )

        buffer.seek(0)

        # Convert PIL → NumPy
        image = Image.open(buffer).convert("RGB")

        image = np.array(image)

        # RGB → OpenCV BGR
        image = cv2.cvtColor(
            image,
            cv2.COLOR_RGB2BGR,
        )

        # Resize for consistent display
        image = cv2.resize(
            image,
            (self.output_size, self.output_size),
            interpolation=cv2.INTER_NEAREST,
        )

        return image