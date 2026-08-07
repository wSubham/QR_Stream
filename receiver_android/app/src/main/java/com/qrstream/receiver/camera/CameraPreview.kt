package com.qrstream.receiver.camera

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner

@Composable
fun CameraPreview(

    onPacketReceived: (ByteArray) -> Unit

) {

    val context = LocalContext.current

    val lifecycleOwner = LocalLifecycleOwner.current

    val previewView = remember {

        PreviewView(context).apply {

            scaleType = PreviewView.ScaleType.FILL_CENTER

        }

    }

    AndroidView(

        factory = {

            previewView

        },

        modifier = Modifier.fillMaxSize()

    )

    DisposableEffect(Unit) {

        val cameraProviderFuture =

            ProcessCameraProvider.getInstance(context)

        val executor =

            ContextCompat.getMainExecutor(context)

        cameraProviderFuture.addListener({

            try {

                val cameraProvider =

                    cameraProviderFuture.get()

                bindCamera(

                    context = context,

                    lifecycleOwner = lifecycleOwner,

                    previewView = previewView,

                    cameraProvider = cameraProvider,

                    onPacketReceived = onPacketReceived

                )

            }

            catch (e: Exception) {

                Log.e(

                    "QRStream",

                    "Camera Error",

                    e

                )

            }

        }, executor)

        onDispose {

            try {

                cameraProviderFuture.get().unbindAll()

            }

            catch (_: Exception) {

            }

        }

    }

}

@SuppressLint("UnsafeOptInUsageError")
private fun bindCamera(

    context: Context,

    lifecycleOwner: LifecycleOwner,

    previewView: PreviewView,

    cameraProvider: ProcessCameraProvider,

    onPacketReceived: (ByteArray) -> Unit

) {

    val preview =

        Preview.Builder()

            .build()

    preview.surfaceProvider =

        previewView.surfaceProvider

    val imageAnalysis =

        ImageAnalysis.Builder()

            .setBackpressureStrategy(

                ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST

            )

            .build()

    imageAnalysis.setAnalyzer(

        ContextCompat.getMainExecutor(context),

        CameraAnalyzer(

            onPacketReceived

        )

    )

    val selector =

        CameraSelector.DEFAULT_BACK_CAMERA

    cameraProvider.unbindAll()

    cameraProvider.bindToLifecycle(

        lifecycleOwner,

        selector,

        preview,

        imageAnalysis

    )

}