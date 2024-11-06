package es.thatapps.fullbus.presentation.busDetails.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import es.thatapps.fullbus.presentation.components.BusStatus
import es.thatapps.fullbus.presentation.components.Header

@Composable
fun BusDetailScreen(
    busLineId: String,
    onBack: () -> Unit,
    navigationToRegister: () -> Unit,
    navigationToSettings: () -> Unit,
    viewModel: BusViewModel = hiltViewModel()
) {
    // Observar el estado de los buses activos
    val activeBuses by viewModel.activeBuses.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Header(navigationToRegister, navigationToSettings)

        LazyColumn(
            modifier = Modifier
                .weight(1f)
        ) {
            items(activeBuses) { bus ->
                BusStatus(
                    busDetail = bus,
                    onReportFull = { viewModel.reportFull(bus.line) },
                    onResetAvailable = { viewModel.setBusAvailable(bus.line) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onBack) {
            Text("Volver a la lista", fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(50.dp))
    }
}