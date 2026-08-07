# QRStream

> **Offline File Transfer System using Dynamic QR Codes**

![Python](https://img.shields.io/badge/Python-3.x-blue)
![Android](https://img.shields.io/badge/Android-Kotlin-green)
![CameraX](https://img.shields.io/badge/CameraX-Enabled-success)
![ML Kit](https://img.shields.io/badge/Google-ML%20Kit-orange)
![OpenCV](https://img.shields.io/badge/OpenCV-4.x-red)
![License](https://img.shields.io/badge/License-MIT-yellow)

---

## Table of Contents

- Overview
- Key Features
- System Architecture
- Workflow
- QRStream Protocol
- Python Sender
- Android Receiver
- Runtime Execution
- Screenshots
- Performance
- Installation
- Project Structure
- Technologies Used
- Contributors
- License

---

# Overview

QRStream is an **offline cross-platform file transfer system** that transfers any file from a desktop computer to an Android device using **dynamic QR codes**.

Unlike conventional transfer methods, QRStream requires **no Internet connection, Wi-Fi, Bluetooth, USB data transfer, or cloud service**. The sender continuously displays QR codes containing encoded file packets, while the Android application scans, validates, reconstructs, and stores the original file.

---

# Key Features

- Offline File Transfer
- Dynamic QR Streaming
- Cross Platform Communication
- Python Sender
- Android Receiver
- CameraX Integration
- Google ML Kit QR Detection
- Custom Binary Protocol
- START / DATA / END Packets
- CRC32 Integrity Verification
- Base64 Packet Encoding
- Chunk-based File Transmission
- Duplicate Chunk Removal
- Ordered Reconstruction
- Metadata Transmission
- UUID Transfer Sessions
- Progress Monitoring
- Missing Chunk Detection
- Automatic File Saving
- Modular Architecture

---

# System Architecture

```text
Input File
    │
    ▼
File Reader
    │
    ▼
Chunk Splitter
    │
    ▼
Packet Builder
    │
    ▼
Serializer
    │
    ▼
Base64 Encoder
    │
    ▼
QR Generator
    │
    ▼
OpenCV Display
═══════════════════════
Android Camera
    │
    ▼
ML Kit Scanner
    │
    ▼
Packet Parser
    │
    ▼
Receiver Core
    │
    ▼
File Reconstruction
    │
    ▼
Downloads/QRStream
```

---

# Workflow

1. Read selected file.
2. Split file into fixed-size chunks.
3. Build START, DATA and END packets.
4. Serialize packet into binary.
5. Encode binary using Base64.
6. Generate QR using Segno.
7. Display QR frames through OpenCV.
8. CameraX captures QR frames.
9. ML Kit decodes QR payload.
10. PacketParser reconstructs protocol packet.
11. ReceiverCore validates CRC32 and stores unique chunks.
12. Missing chunks are detected.
13. FileWriter reconstructs and stores the original file.

---

# QRStream Protocol

```text
+----------------------------------------------------------------+
| MAGIC | VERSION | TYPE | UUID | CHUNK | TOTAL | SIZE | CRC32 |
+----------------------------------------------------------------+
|                          PAYLOAD                              |
+----------------------------------------------------------------+
```

Packet Types

- START : File metadata
- DATA : Binary file chunks
- END : Transfer completion

---

# Python Sender

The Python sender handles complete transmission.

Core responsibilities:

- Read file
- Split file
- Build packets
- Serialize protocol
- Generate QR codes
- Display QR stream
- Control transmission timing

---

# Android Receiver

The Android application continuously scans QR codes and reconstructs the transmitted file.

Core responsibilities:

- Camera capture
- QR detection
- Base64 decoding
- Packet parsing
- CRC verification
- Duplicate filtering
- Chunk reconstruction
- File saving
- Progress monitoring

---

# Runtime Execution

```text
File
 │
 ▼
Read
 │
 ▼
Split
 │
 ▼
Packet
 │
 ▼
Serialize
 │
 ▼
Base64
 │
 ▼
QR Generation
 │
 ▼
Display
══════════════════════
Android Camera
 │
 ▼
Decode
 │
 ▼
Packet Parser
 │
 ▼
Receiver Core
 │
 ▼
Reconstruct
 │
 ▼
Downloads/QRStream
```

---

# Screenshots

Add your screenshots here:

- Sender Interface
- Receiver Waiting
- Receiving Progress
- Missing Chunk Detection
- Transfer Completed
- Saved File in Downloads

---

# Performance

| Parameter | Value |
|-----------|-------|
| Chunk Size | 700 Bytes |
| QR Generator | Segno |
| QR Detection | Google ML Kit |
| Camera | CameraX |
| Integrity Check | CRC32 |
| Communication | Offline |
| Platform | Python + Android |

---

# Installation

## Clone

```bash
git clone https://github.com/<username>/QR_Stream.git
cd QR_Stream
```

## Python Sender

```bash
python -m venv venv
```

Windows

```bash
venv\Scripts\activate
```

Install requirements

```bash
pip install -r requirements.txt
```

Run

```bash
python -m sender.app
```

## Android Receiver

Open `receiver_android` in Android Studio.

Build and install the APK on an Android device.

---

# Project Structure

```text
QR_Stream
├── docs/
├── output/
├── received/
├── receiver_android/
│   └── app/
├── samples/
├── sender/
├── tests/
├── requirements.txt
├── README.md
├── LICENSE
└── .gitignore
```

---

# Technologies Used

- Python
- Kotlin
- Jetpack Compose
- CameraX
- Google ML Kit
- OpenCV
- Segno
- NumPy

---

# Contributors

- Subham Das

---

# License

This project is released under the MIT License.
