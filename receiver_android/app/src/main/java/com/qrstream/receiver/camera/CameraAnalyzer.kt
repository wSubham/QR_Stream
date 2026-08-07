package com.qrstream.receiver.camera

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.qrstream.receiver.utils.Base64Utils

class CameraAnalyzer(

    private val onPacketReceived: (ByteArray) -> Unit

) : ImageAnalysis.Analyzer {

    private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_QR_CODE
        )
        .build()

    private val scanner = BarcodeScanning.getClient(options)

    /**
     * Last QR decoded.
     * Prevents processing the same QR frame repeatedly.
     */
    private var lastQR: String? = null

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {

        val mediaImage = imageProxy.image

        if (mediaImage == null) {
            imageProxy.close()
            return
        }

        val image = InputImage.fromMediaImage(
            mediaImage,
            imageProxy.imageInfo.rotationDegrees
        )

        scanner.process(image)

            .addOnSuccessListener { barcodes ->

                if (barcodes.isEmpty()) {
                    return@addOnSuccessListener
                }

                val qrText = barcodes.first().rawValue
                    ?: return@addOnSuccessListener

                /**
                 * Ignore duplicate scans
                 */
                if (qrText == lastQR) {
                    return@addOnSuccessListener
                }

                lastQR = qrText

                try {

                    val packetBytes =
                        Base64Utils.decode(qrText)

                    onPacketReceived(packetBytes)

                } catch (_: Exception) {

                }

            }

            .addOnCompleteListener {

                imageProxy.close()

            }

    }

}