package com.qrstream.receiver

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.qrstream.receiver.camera.CameraScreen

class MainActivity : ComponentActivity() {

    private val hasPermission = mutableStateOf(false)

    private val launcher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {

            hasPermission.value = it

        }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        hasPermission.value =

            ContextCompat.checkSelfPermission(

                this,

                Manifest.permission.CAMERA

            ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission.value) {

            launcher.launch(

                Manifest.permission.CAMERA

            )

        }

        setContent {

            MaterialTheme {

                if (hasPermission.value) {

                    CameraScreen()

                } else {

                    Box(

                        modifier = Modifier.fillMaxSize(),

                        contentAlignment = Alignment.Center

                    ) {

                        Text(

                            "Camera Permission Required"

                        )

                    }

                }

            }

        }

    }

}