package com.qrstream.receiver.protocol

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.UUID

object PacketParser {

    private const val MAGIC = "QRTP"

    private const val HEADER_SIZE = 36

    fun parse(packetBytes: ByteArray): Packet {

        require(packetBytes.size >= HEADER_SIZE) {
            "Packet too small"
        }

        val buffer = ByteBuffer
            .wrap(packetBytes)
            .order(ByteOrder.BIG_ENDIAN)

        //-------------------------
        // MAGIC
        //-------------------------

        val magicBytes = ByteArray(4)

        buffer.get(magicBytes)

        val magic = String(magicBytes)

        require(magic == MAGIC) {
            "Invalid Protocol"
        }

        //-------------------------
        // VERSION
        //-------------------------

        val version = buffer.get().toInt() and 0xFF

        //-------------------------
        // TYPE
        //-------------------------

        val packetTypeValue = buffer.get().toInt() and 0xFF

        val packetType =
            PacketType.fromValue(packetTypeValue)

        //-------------------------
        // UUID
        //-------------------------

        val uuidBytes = ByteArray(16)

        buffer.get(uuidBytes)

        val uuidBuffer =
            ByteBuffer.wrap(uuidBytes)

        val transferId = UUID(

            uuidBuffer.long,

            uuidBuffer.long

        )

        //-------------------------
        // CHUNK NUMBER
        //-------------------------

        val chunkNumber = buffer.int

        //-------------------------
        // TOTAL CHUNKS
        //-------------------------

        val totalChunks = buffer.int

        //-------------------------
        // PAYLOAD SIZE
        //-------------------------

        val payloadSize =
            buffer.short.toInt() and 0xFFFF

        //-------------------------
        // CRC32
        //-------------------------

        val crc32 =
            buffer.int.toLong() and 0xffffffffL

        //-------------------------
        // PAYLOAD
        //-------------------------

        require(payloadSize >= 0) {

            "Invalid payload size"

        }

        require(

            buffer.remaining() >= payloadSize

        ) {

            "Corrupted packet"

        }

        val payload = ByteArray(payloadSize)

        buffer.get(payload)

        //-------------------------
        // RETURN
        //-------------------------

        return Packet(

            packetType = packetType,

            version = version,

            transferId = transferId,

            chunkNumber = chunkNumber,

            totalChunks = totalChunks,

            payload = payload,

            crc32 = crc32

        )

    }

}