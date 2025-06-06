package es.thatapps.fullbus.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.thatapps.fullbus.R
import es.thatapps.fullbus.presentation.busDetails.domain.BusDetailDomain
import es.thatapps.fullbus.ui.theme.Green
import es.thatapps.fullbus.ui.theme.Orange
import es.thatapps.fullbus.ui.theme.Red
import es.thatapps.fullbus.ui.theme.Red2
import es.thatapps.fullbus.utils.ImageBase64

@Composable
fun              BusStatus(
    busDetail: BusDetailDomain,
    onReportFull: () -> Unit,
    pfp : String,
    username : String,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (busDetail.isFull) Red else Green,
                RoundedCornerShape(16.dp)
            )
            .border(3.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(16.dp))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Información de la línea y la hora
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
        ) {
            // Línea del autobús y hora de salida
            Row(
                modifier = Modifier.fillMaxWidth(), // Ocupa el ancho disponible
            ) {
                // Línea del autobús
                Column(
                    modifier = Modifier.weight(1f), // Ocupa el espacio a la izquierda
                    horizontalAlignment = Alignment.Start
                ) {
                    // Hora de salida del autobús
                    Clock(
                        time = busDetail.departureTime,
                    )
                }

                // Imagen alineada a la derecha
                if (busDetail.isFull)
                    Image(
                    painter = painterResource(id = R.drawable.advertencia),
                    contentDescription = "Line Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(50.dp)
                )
            }
        }

        // Imagen de bus
        Image(
            painter = painterResource(id = R.drawable.bus_1),
            contentDescription = "Bus Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(210.dp)
        )

        Text(
            text = busDetail.line,
            fontSize = 28.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Estado y acciones
        if (busDetail.isFull) {
            Row(
                modifier = Modifier
                    .background(Orange, RoundedCornerShape(20.dp))
                    .border(3.dp, Color.Black, RoundedCornerShape(20.dp))
                    .padding(8.dp)
                    .wrapContentSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .shadow(6.dp, RoundedCornerShape(50))
                ) {
                    ImageBase64(imageUrl = pfp, size = 46.dp)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Reportado por $username",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        } else {
            Button(
                onClick = onReportFull,
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(3.dp, Color.Black),
                colors = ButtonDefaults.buttonColors(containerColor = Red2),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(width = 230.dp, height = 48.dp)
                ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    tint = Color.Black,
                    contentDescription = "Report Full",
                    modifier = Modifier.size(28.dp)
                )

                Spacer(modifier = Modifier.width(4.dp)) // Separacion entre el icono y el texto

                Text(
                    text = "Reportar",
                    color = Color.Black,
                    fontSize = 20.sp
                )
            }
        }
    }
}