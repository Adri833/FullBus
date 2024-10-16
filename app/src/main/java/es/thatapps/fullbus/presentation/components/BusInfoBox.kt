package es.thatapps.fullbus.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.thatapps.fullbus.R

@Composable
fun BusInfoBox(
    // Constructores
    lineNumber: String,
    backgroundColor: Color,
    origin: String,
    destination: String,
    busIcon: Int,
    reversed: Boolean = false // Invierte el orden
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, shape = RoundedCornerShape(16.dp))
            .border(2.dp, Color.Black, shape = RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Número de línea en un círculo
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(Color.White, shape = CircleShape)
                .border(2.dp, Color.Black, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = lineNumber, color = Color.Black)
        }

        // Columna para origen y destino con la flecha
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center // Centra horizontalmente
        ) {
            if (reversed) {

                // Destino
                Text(text = destination, fontSize = 22.sp ,color = Color.Black)

                Spacer(modifier = Modifier.width(12.dp))

                // Ícono de flecha
                Image(
                    painter = painterResource(id = R.drawable.ic_arrow_reverse),
                    contentDescription = "Arrow Icon",
                    modifier = Modifier.size(38.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Origen
                Text(text = origin, fontSize = 22.sp ,color = Color.Black)
            } else {
                // Orden normal
                Text(text = origin, fontSize = 22.sp ,color = Color.Black)

                Spacer(modifier = Modifier.width(12.dp))

                // Ícono de flecha
                Image(
                    painter = painterResource(id = R.drawable.ic_arrow),
                    contentDescription = "Arrow Icon",
                    modifier = Modifier.size(38.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Destino
                Text(text = destination, fontSize = 22.sp ,color = Color.Black)
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Icono del bus a la derecha
            Image(
                painter = painterResource(id = busIcon),
                contentDescription = "Bus Icon",
                modifier = Modifier.size(60.dp)
            )
        }
    }
}
