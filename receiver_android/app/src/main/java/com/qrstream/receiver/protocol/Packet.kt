package com.qrstream.receiver.protocol

import java.util.UUID

/**
 * QRStream Packet
 *
 * Matches the Python sender protocol exactly.
 */
data class Packet(

    val packetType: PacketType,

    val version: Int,

    val transferId: UUID,

    val chunkNumber: Int,

    val totalChunks: Int,

    val payload: ByteArray,

    val crc32: Long

)

/**
 * Packet Types
 *
 * START = Metadata
 * DATA  = File chunk
 * END   = Transfer finished
 */
enum class PacketType(val value: Int) {

    START(1),

    DATA(2),

    END(3);

    companion object {

        fun fromValue(value: Int): PacketType {

            return when (value) {

                1 -> START

                2 -> DATA

                3 -> END

                else -> throw IllegalArgumentException(
                    "Unknown Packet Type: $value"
                )

            }

        }

    }

}