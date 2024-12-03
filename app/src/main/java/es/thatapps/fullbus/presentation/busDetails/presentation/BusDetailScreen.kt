package es.thatapps.fullbus.presentation.busDetails.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import es.thatapps.fullbus.presentation.components.AdBanner
import es.thatapps.fullbus.presentation.components.Header
import es.thatapps.fullbus.presentation.components.HorizontalPagerBuses
import es.thatapps.fullbus.presentation.components.adjustForMobile
import kotlinx.coroutines.delay

@Composable
fun BusDetailScreen(
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

        // Contenedor para centrar el texto de la hora
        Box(
            modifier = Modifier
                .fillMaxWidth() // Ocupa todo el ancho
                .padding(top = 8.dp), // Espaciado vertical
            contentAlignment = Alignment.Center // Centra el contenido
        ) {
            Text(
                text = currentTime.value,
                fontSize = 20.sp
            )
        }

        // Contenedor para el HorizontalPager
        Box(
            modifier = Modifier
                .weight(1f) // Hace que ocupe el espacio disponible
                .padding(top = 16.dp, start = 16.dp, end = 16.dp) // Separación con el texto de la hora
        ) {
            HorizontalPagerBuses(activeBuses, viewModel)
        }

        Spacer(modifier = Modifier.height(16.dp))

        AdBanner(context = LocalContext.current)
    }
}