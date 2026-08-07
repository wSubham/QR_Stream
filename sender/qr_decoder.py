"""
QR Decoder

Reads QR images and extracts packet bytes.
"""

"""
QR Decoder
"""

import base64
import cv2


class QRDecoder:

    def __init__(self):

        self.detector = cv2.QRCodeDetector()

    def decode(self, image):

        text, points, _ = self.detector.detectAndDecode(image)

        if points is None:
            return None

        if text == "":
            return None

        try:

            packet = base64.b64decode(text)

            return packet

        except Exception:

            return None