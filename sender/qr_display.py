"""
QR Display Window
"""

import cv2
import numpy as np

from sender.config import FRAME_DELAY


class QRDisplay:

    WINDOW_NAME = "QRStream Sender"

    def __init__(self):

        cv2.namedWindow(
            self.WINDOW_NAME,
            cv2.WINDOW_NORMAL,
        )

        cv2.resizeWindow(
            self.WINDOW_NAME,
            520,
            620,
        )

    def show(
        self,
        frame,
        title="",
        chunk=0,
        total=0,
    ):

        h, w = frame.shape[:2]

        header = 120

        canvas = np.full(
            (h + header, w, 3),
            255,
            dtype=np.uint8
        )

        canvas[header:, :] = frame

        progress = 0

        if total > 0:
            progress = int(chunk * 100 / total)

        cv2.putText(
            canvas,
            "QRStream Sender",
            (15, 30),
            cv2.FONT_HERSHEY_SIMPLEX,
            0.7,
            (30, 30, 30),
            2,
        )

        cv2.putText(
            canvas,
            f"Status : {title}",
            (15, 58),
            cv2.FONT_HERSHEY_SIMPLEX,
            0.60,
            (0, 150, 0),
            2,
        )

        cv2.putText(
            canvas,
            f"Chunk : {chunk} / {total}",
            (15, 84),
            cv2.FONT_HERSHEY_SIMPLEX,
            0.55,
            (0, 0, 0),
            2,
        )

        cv2.putText(
            canvas,
            f"Progress : {progress} %",
            (15, 108),
            cv2.FONT_HERSHEY_SIMPLEX,
            0.50,
            (100, 100, 100),
            1,
        )

        # Progress bar
        bar_x = 250
        bar_y = 95
        bar_w = 150
        bar_h = 12

        cv2.rectangle(
            canvas,
            (bar_x, bar_y),
            (bar_x + bar_w, bar_y + bar_h),
            (180, 180, 180),
            1,
        )

        if total > 0:

            fill = int(bar_w * chunk / total)

            cv2.rectangle(
                canvas,
                (bar_x, bar_y),
                (bar_x + fill, bar_y + bar_h),
                (0, 180, 0),
                -1,
            )

        cv2.line(
            canvas,
            (0, header - 1),
            (w, header - 1),
            (200, 200, 200),
            1,
        )

        cv2.imshow(
            self.WINDOW_NAME,
            canvas,
        )

        key = cv2.waitKey(FRAME_DELAY) & 0xFF

        if key == 27:
            return False

        return True

    def close(self):

        cv2.destroyAllWindows()