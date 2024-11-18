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
import kotlinx.coroutines.delay

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

    // Estado para la hora actual
    val currentTime = remember { mutableStateOf(viewModel.getCurrentHour()) }

    // Actualiza la hora cada segundo
    LaunchedEffect(Unit) {
        while (true) {
            currentTime.value = viewModel.getCurrentHour()
            delay(1000)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Header(navigationToRegister, navigationToSettings)

        Text("Dia actual: ${viewModel.getLogicDay()}", fontSize = 20.sp)

        Text("Hora actual: ${currentTime.value}", fontSize = 20.sp)

        LazyColumn(
            modifier = Modifier
                .weight(1f)
        ) {
            items(activeBuses) { bus ->
                BusStatus(
                    busDetail = bus,
                    onReportFull = { viewModel.reportFull(bus.line) },
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