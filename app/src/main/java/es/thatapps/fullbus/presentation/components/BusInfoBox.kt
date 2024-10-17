package es.thatapps.fullbus.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.thatapps.fullbus.R

@Composable
fun BusInfoBox(
    lineNumber: String,
    backgroundColor1: Color,
    backgroundColor2: Color,
    origin: String,
    destination: String,
    busIcon: Int,
    onClick: () -> Unit // Maneja el click
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(16.dp)) // Sombra
            .border(2.dp, Color.Black, shape = RoundedCornerShape(16.dp)) // Borde negro
            .clickable(onClick = onClick), // Hacer que el Card sea clicable
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent // Fondo transparente para el gradiente
        )
    ) {
        // Colores gradientes
        Box(
            modifier = Modifier
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            backgroundColor1,
                            backgroundColor2
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Número de línea en un círculo con borde
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .shadow(6.dp, shape = CircleShape) // Agrega sombra
                        .background(Color.White, shape = CircleShape)
                        .border(2.5.dp, Color.Black, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    // Texto del circulo
                    Text(
                        text = lineNumber,
                        fontSize = 16.sp,
                        color = Color.Black,
                        style = androidx.compose.ui.text.TextStyle( // negrita
                            fontWeight = Bold))
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Columna para origen y destino con la flecha
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = origin, fontSize = 22.sp, color = Color.Black)
                    Spacer(modifier = Modifier.width(8.dp))
                    Image(
                        painter = painterResource(id = R.drawable.ic_arrow2),
                        contentDescription = "Arrow Icon",
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = destination, fontSize = 22.sp, color = Color.Black)
                }


                Spacer(modifier = Modifier.width(16.dp))

                // Ícono del bus a la derecha
                Image(
                    painter = painterResource(id = busIcon),
                    contentDescription = "Bus Icon",
                    modifier = Modifier.size(45.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewBusInfoBox() {
    BusInfoBox(
        lineNumber = "M-126",
        backgroundColor1 = Color(0xFFffb9d3),
        backgroundColor2 = Color(0xFFffa2c4),
        origin = "Viso",
        destination = "Sevilla",
        busIcon = R.drawable.ic_bus,
        onClick = {}
    )
}
