"""
QRStream Configuration
"""

from pathlib import Path

# ==============================
# QR SETTINGS
# ==============================

# Payload bytes carried in each DATA packet
CHUNK_SIZE = 700

# QR Error Correction
QR_ERROR = "M"

# QR Scale (pixel size)
QR_SCALE = 6

# QR Border
QR_BORDER = 2

# ==============================
# DISPLAY SETTINGS
# ==============================

# DISPLAY_FPS = 15

# FRAME_DELAY = int(1000 / DISPLAY_FPS)

# # START packet repetitions
# START_REPEAT = 10

# # END packet repetitions
# END_REPEAT = 10

DISPLAY_FPS = 8

FRAME_DELAY = 125

START_REPEAT = 20

END_REPEAT = 20

# ==============================
# PATHS
# ==============================

ROOT_DIR = Path(__file__).resolve().parent.parent

OUTPUT_DIR = ROOT_DIR / "output"

OUTPUT_DIR.mkdir(exist_ok=True)