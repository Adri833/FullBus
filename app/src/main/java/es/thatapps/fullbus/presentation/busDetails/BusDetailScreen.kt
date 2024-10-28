package es.thatapps.fullbus.presentation.home.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BusDetailScreen(
    busLineId: String,
    onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Detalles de la línea: $busLineId")

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = onBack) {
            Text("Volver a la lista", fontSize = 20.sp)
        }
    }
}
