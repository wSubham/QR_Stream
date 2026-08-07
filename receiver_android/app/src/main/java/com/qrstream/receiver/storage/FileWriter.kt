package com.qrstream.receiver.storage

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.TreeMap

object FileWriter {

    /**
     * Saves the reconstructed file into:
     *
     * Downloads/
     *      QRStream/
     *          filename
     */

    fun save(

        context: Context,

        filename: String,

        chunks: Map<Int, ByteArray>

    ): File {

        val orderedChunks = TreeMap(chunks)

        // -----------------------------
        // Android 10+
        // -----------------------------

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            val values = ContentValues().apply {

                put(
                    MediaStore.Downloads.DISPLAY_NAME,
                    filename
                )

                put(
                    MediaStore.Downloads.MIME_TYPE,
                    "application/octet-stream"
                )

                put(
                    MediaStore.Downloads.RELATIVE_PATH,
                    Environment.DIRECTORY_DOWNLOADS + "/QRStream"
                )

                put(
                    MediaStore.Downloads.IS_PENDING,
                    1
                )

            }

            val resolver = context.contentResolver

            val uri = resolver.insert(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                values
            ) ?: throw Exception("Unable to create file.")

            resolver.openOutputStream(uri)?.use { stream ->

                writeChunks(stream, orderedChunks)

            }

            values.clear()

            values.put(
                MediaStore.Downloads.IS_PENDING,
                0
            )

            resolver.update(
                uri,
                values,
                null,
                null
            )

            return File(
                Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS
                ),
                "QRStream/$filename"
            )

        }

        // -----------------------------
        // Android 9 and below
        // -----------------------------

        val folder = File(

            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS
            ),

            "QRStream"

        )

        if (!folder.exists()) {

            folder.mkdirs()

        }

        val file = File(folder, filename)

        FileOutputStream(file).use { stream ->

            writeChunks(stream, orderedChunks)

        }

        return file

    }

    // ---------------------------------------------

    private fun writeChunks(

        stream: OutputStream,

        chunks: TreeMap<Int, ByteArray>

    ) {

        for ((_, data) in chunks) {

            stream.write(data)

        }

        stream.flush()

    }

}