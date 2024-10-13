package es.thatapps.fullbus.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.thatapps.fullbus.R

@Composable
fun BusStatusBox(time: String, isFull: Boolean, reportCount: Int = 0, onReportFull: () -> Unit) {
    val backgroundColor =
        if (isFull) Color(0xFFB71C1C) else Color(0xFF2E7D32) // Colores segun si esta lleno o no
    val statusText = if (isFull) "❌LLENO" else "✅LIBRE" // Textos segun si esta libre o no

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, shape = RoundedCornerShape(12.dp))
            .border(2.dp, color = Color.Black, shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen del autobus
            Image(
                painter = painterResource(id = R.drawable.autobus),
                contentDescription = "Foto de un bus",
                modifier = Modifier.size(55.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Información del estado del autobus
            Column {
                Text(text = time, fontSize = 18.sp, color = Color.White)
                Text(text = statusText, fontSize = 14.sp, color = Color.White)
                if (isFull) {
                    Text(
                        text = "Lleno desde (nombre_parada)",
                        fontSize = 12.sp,
                        color = Color.White
                    )
                    Text(
                        text = "Reportado por (nombre de usuario)",
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botón para reportar si está lleno
            IconButton(
                onClick = onReportFull,
                modifier = Modifier
                    .background(Color.White, shape = CircleShape)
                    .size(50.dp)
            ) {
                // Modificaciones si esta lleno o no
                if (isFull) {
                    Text(text = "👍 $reportCount", color = Color.Black)
                } else {
                    Text(text = "⛔")
                }
            }
        }
    }
}