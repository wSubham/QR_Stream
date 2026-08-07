package com.qrstream.receiver.protocol

import org.json.JSONObject
import java.util.TreeMap
import java.util.zip.CRC32

class ReceiverCore {

    private var metadata: JSONObject? = null

    private val chunks = TreeMap<Int, ByteArray>()

    private var totalChunks = 0

    private var transferFinished = false

    // --------------------------------------------

    fun receive(packet: Packet) {

        verifyCRC(packet)

        when (packet.packetType) {

            PacketType.START -> receiveStart(packet)

            PacketType.DATA -> receiveData(packet)

            PacketType.END -> receiveEnd()

        }

    }

    // --------------------------------------------

    private fun verifyCRC(packet: Packet) {

        val crc = CRC32()

        crc.update(packet.payload)

        if (crc.value != packet.crc32) {

            throw RuntimeException("CRC Verification Failed")

        }

    }

    // --------------------------------------------

    private fun receiveStart(packet: Packet) {

        if (metadata != null) {
            return
        }

        metadata = JSONObject(String(packet.payload))

        totalChunks = metadata!!.getInt("total_chunks")

        chunks.clear()

        transferFinished = false

    }

    // --------------------------------------------

    private fun receiveData(packet: Packet) {

        // Ignore duplicate QR scans
        if (chunks.containsKey(packet.chunkNumber)) {
            return
        }

        chunks[packet.chunkNumber] = packet.payload

    }

    // --------------------------------------------

    private fun receiveEnd() {

        transferFinished = true

    }

    // --------------------------------------------

    fun completed(): Boolean {

        return transferFinished &&
                chunks.size == totalChunks

    }

    // --------------------------------------------

    fun getReceivedChunkCount(): Int {

        return chunks.size

    }

    // --------------------------------------------

    fun getTotalChunks(): Int {

        return totalChunks

    }

    // --------------------------------------------

    fun getMissingChunks(): List<Int> {

        val missing = mutableListOf<Int>()

        for (i in 0 until totalChunks) {

            if (!chunks.containsKey(i)) {

                missing.add(i)

            }

        }

        return missing

    }

    // --------------------------------------------

    fun getMetadata(): JSONObject? {

        return metadata

    }

    // --------------------------------------------

    fun getChunks(): Map<Int, ByteArray> {

        return chunks

    }

}