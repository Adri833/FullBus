package es.thatapps.fullbus.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.thatapps.fullbus.R
import es.thatapps.fullbus.presentation.busDetails.domain.BusDetailDomain

@Composable
fun BusStatus(
    busDetail: BusDetailDomain,
    onReportFull: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (busDetail.isFull) Color(0xFFFFCDD2) else Color(0xFFC8E6C9),
                RoundedCornerShape(8.dp)
            )
            .border(2.dp, Color.Black, RoundedCornerShape(8.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Información de la línea y la hora
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
        ) {
            // Línea del autobús
            Text(
                text = busDetail.line,
                fontSize = 28.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Hora de salida del autobús
            Text(
                text = busDetail.departureTime,
                fontSize = 34.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Imagen de bus
        Image(
            painter = painterResource(id = R.drawable.bus_1), // Asegúrate de que la imagen exista
            contentDescription = "Bus Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Estado y acciones
        if (busDetail.isFull) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Full Status",
                    tint = Color.Red,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Lleno",
                    fontSize = 20.sp,
                    color = Color.Red
                )
            }
        } else {
            Button(
                onClick = onReportFull,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFD4655)),
                modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Report Full",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Reportar como lleno")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBusStatus() {
    BusStatus(
        busDetail = BusDetailDomain(
            line = "M-126",
            departureTime = "15:30",
            arriveTime = "16:30",
            isFull = false
        ),
        onReportFull = { /* Acción para reportar como lleno */ },
    )
}
