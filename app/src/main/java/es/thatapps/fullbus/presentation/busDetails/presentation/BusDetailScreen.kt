package es.thatapps.fullbus.presentation.busDetails.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.ui.platform.LocalContext
import es.thatapps.fullbus.presentation.components.AdBanner
import es.thatapps.fullbus.presentation.components.BusStatus
import es.thatapps.fullbus.presentation.components.Header
import es.thatapps.fullbus.presentation.components.adjustForMobile
import kotlinx.coroutines.delay

@Composable
fun BusDetailScreen(
    busLineId: String,
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
            .adjustForMobile()
    ) {
        // Encabezado
        Header(navigationToRegister, navigationToSettings)

        // Textos de prueba para el dia y la hora actual
        Text("Dia actual: ${viewModel.getLogicDay()}", fontSize = 20.sp)
        Text("Hora actual: ${currentTime.value}", fontSize = 20.sp)

        // Contenedor para el HorizontalPager
        Box(
            modifier = Modifier
                .weight(1f) // Hace que ocupe el espacio disponible
                .padding(top = 16.dp, start = 16.dp, end = 16.dp) // Separación con el texto de la hora
        ) {
            HorizontalPager(
                state = rememberPagerState(pageCount = { activeBuses.size }),
                modifier = Modifier.fillMaxSize() // Asegura que ocupe el espacio del Box al completo
            ) { page ->
                // Mostrar el bus correspondiente a la pagina actual
                val bus = activeBuses[page]
                BusStatus(
                    busDetail = bus,
                    onReportFull = { viewModel.reportFull(bus.line) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AdBanner(context = LocalContext.current)
    }
}