package com.qrstream.receiver.utils

import android.util.Base64

object Base64Utils {

    /**
     * Decode Base64 QR text into raw packet bytes.
     */
    fun decode(text: String): ByteArray {

        return Base64.decode(

            text,

            Base64.DEFAULT

        )

    }

    /**
     * Encode bytes into Base64.
     * (Useful for testing/debugging)
     */
    fun encode(bytes: ByteArray): String {

        return Base64.encodeToString(

            bytes,

            Base64.NO_WRAP

        )

    }

}