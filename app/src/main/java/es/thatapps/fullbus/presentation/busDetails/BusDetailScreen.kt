package es.thatapps.fullbus.presentation.home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import es.thatapps.fullbus.R
import es.thatapps.fullbus.presentation.busDetails.BusViewModel
import es.thatapps.fullbus.presentation.components.AdBanner
import es.thatapps.fullbus.presentation.components.Header

@Composable
fun BusDetailScreen(
    busLineId: String,
    onBack: () -> Unit,
    navigationToRegister: () -> Unit,
    navigationToSettings: () -> Unit,
    viewModel: BusViewModel = hiltViewModel(),
) {
    // Estado para controlar si el autobús está disponible o lleno
    var isBusFull by remember { mutableStateOf(false) }

    // LaunchedEffect para recolectar el estado solo cuando cambia el ID de la línea
    LaunchedEffect(busLineId) {
        viewModel.getBusFullState(busLineId).collect { state ->
            isBusFull = state
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Header(navigationToRegister, navigationToSettings)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                text = busLineId,
                fontSize = 24.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (isBusFull) Color(0xFFFFCDD2) else Color(0xFFC8E6C9),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.bus_1),
                    contentDescription = "Imagen de autobús",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "7:00",
                    fontSize = 40.sp,
                    color = Color.Black
                )

                if (isBusFull) {
                    Text(
                        text = "Lleno",
                        fontSize = 24.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Reportado por tupadre33 en el viso",
                        color = Color.Black,
                        modifier = Modifier
                            .background(Color(0xFFFFCF74), RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    )
                } else {
                    Button(
                        onClick = { viewModel.reportBusFull(busLineId) },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Reportar como lleno")
                    }
                }

                // Botón para restablecer el estado a libre (disponible)
                Button(
                    onClick = { viewModel.setBusAvailable(busLineId) }, // Método que debes implementar en tu ViewModel
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                ) {
                    Text("Restablecer a libre")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(Color.Green, RoundedCornerShape(50))
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Siguiente bus - 7:15",
                    fontSize = 20.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.width(16.dp))

                Row {
                    Text("✅", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("❌", fontSize = 24.sp)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(onClick = onBack) {
                Text("Volver a la lista", fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.height(34.dp))
        }
    }
}
