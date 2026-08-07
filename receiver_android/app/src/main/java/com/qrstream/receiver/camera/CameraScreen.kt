package com.qrstream.receiver.camera

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.qrstream.receiver.protocol.PacketParser
import com.qrstream.receiver.protocol.PacketType
import com.qrstream.receiver.protocol.ReceiverCore
import com.qrstream.receiver.storage.FileWriter
import com.qrstream.receiver.ui.StatusOverlay

@Composable
fun CameraScreen() {

    val context = LocalContext.current

    val receiver = remember {
        ReceiverCore()
    }

    var status by remember {
        mutableStateOf("Waiting for QR...")
    }

    var receivedChunks by remember {
        mutableStateOf(0)
    }

    var totalChunks by remember {
        mutableStateOf(0)
    }

    var fileSaved by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        CameraPreview(

            onPacketReceived = { packetBytes ->

                try {

                    val packet = PacketParser.parse(packetBytes)

                    receiver.receive(packet)

                    status = packet.packetType.name

                    when (packet.packetType) {

                        PacketType.START -> {

                            receivedChunks = 0

                            totalChunks = receiver.getTotalChunks()

                            fileSaved = false

                        }

                        PacketType.DATA -> {

                            receivedChunks =
                                receiver.getReceivedChunkCount()

                            totalChunks =
                                receiver.getTotalChunks()

                        }

                        PacketType.END -> {

                            val missing =
                                receiver.getMissingChunks()

                            if (missing.isNotEmpty()) {

                                status =
                                    "Missing ${missing.size} Chunks"

                                return@CameraPreview

                            }

                        }

                    }

                    if (receiver.completed() && !fileSaved) {

                        val meta =
                            receiver.getMetadata()

                        val filename =
                            meta?.optString(
                                "filename",
                                "received.bin"
                            ) ?: "received.bin"

                        try {

                            val savedFile =
                                FileWriter.save(

                                    context = context,

                                    filename = filename,

                                    chunks = receiver.getChunks()

                                )

                            fileSaved = true

                            status = "Completed ✓"

                            Toast.makeText(

                                context,

                                "Saved to:\n${savedFile.absolutePath}",

                                Toast.LENGTH_LONG

                            ).show()

                        } catch (e: Exception) {

                            status = "Save Failed"

                            Toast.makeText(

                                context,

                                "Save Error:\n${e.message}",

                                Toast.LENGTH_LONG

                            ).show()

                        }

                    }

                } catch (e: Exception) {

                    status =
                        e.message ?: "Unknown Error"

                }

            }

        )

        StatusOverlay(

            status = status,

            received = receivedChunks,

            total = totalChunks

        )

    }

}