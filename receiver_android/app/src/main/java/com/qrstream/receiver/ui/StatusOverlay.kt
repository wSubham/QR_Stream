package com.qrstream.receiver.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StatusOverlay(

    status: String,

    received: Int,

    total: Int

) {

    val progress =
        if (total == 0)
            0f
        else
            received.toFloat() / total.toFloat()

    Column(

        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)

    ) {

        Card {

            Column(

                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),

                verticalArrangement = Arrangement.spacedBy(10.dp)

            ) {

                Text(

                    text = "QRStream Receiver",

                    style = MaterialTheme.typography.titleLarge

                )

                Text(

                    text = "Status : $status"

                )

                Text(

                    text = "Received : $received / $total"

                )

                LinearProgressIndicator(

                    progress = { progress },

                    modifier = Modifier.fillMaxWidth()

                )

                Text(

                    text = "${(progress * 100).toInt()} %"

                )

            }

        }

    }

}