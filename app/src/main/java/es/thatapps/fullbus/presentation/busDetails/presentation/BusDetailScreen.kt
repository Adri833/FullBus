package es.thatapps.fullbus.presentation.busDetails.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import es.thatapps.fullbus.presentation.components.AdBanner
import es.thatapps.fullbus.presentation.components.Header
import es.thatapps.fullbus.presentation.components.HorizontalPagerBuses
import es.thatapps.fullbus.presentation.components.adjustForMobile
import kotlinx.coroutines.delay

@SuppressLint("DiscouragedApi")
@Composable
fun BusDetailScreen(
    busLine: String,
    navigationToRegister: () -> Unit,
    navigationToSettings: () -> Unit,
    viewModel: BusViewModel = hiltViewModel()
) {
    // Observar el estado de los buses activos
    val activeBuses by viewModel.activeBuses.collectAsState()
    val filteredBuses = activeBuses.filter { it.line == busLine }
    val idaBuses = filteredBuses.filter { it.direction == "Ida" }
    val vueltaBuses = filteredBuses.filter { it.direction == "Vuelta" }

    // Estado para la hora actual
    val currentTime = remember { mutableStateOf(viewModel.getCurrentHour()) }

    // Actualiza la hora cada segundo
    LaunchedEffect(Unit) {
        while (true) {
            currentTime.value = viewModel.getCurrentHour()
            delay(1000)
        }
    }

    // Estado para la hora seleccionada
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Ida", "Vuelta", "Horario")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .adjustForMobile()
    ) {
        // Encabezado
        Header(navigationToRegister, navigationToSettings)

        // Navegacion superior
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text (text = title) }
                )
            }
        }

        // Contenido dinámico según la pestaña seleccionada
        when (selectedTabIndex) {
            0 -> { // Ida

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
                        .padding(16.dp) // Separación con el texto de la hora
                ) {
                    HorizontalPagerBuses(idaBuses, viewModel)
                }
            }

            1 -> { // Vuelta

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

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    HorizontalPagerBuses(vueltaBuses , viewModel)
                }
            }

            2 -> { // Horario
                Box(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    // Formatea el nombre del recurso
                    val resourceName = "horario_${busLine.replace("-", "").lowercase()}"
                    val resourceId = LocalContext.current.resources.getIdentifier(resourceName, "drawable", LocalContext.current.packageName)

                    // Muestra la imagen si existe
                    if (resourceId != 0) {
                        Image(
                            painter = painterResource(id = resourceId),
                            contentDescription = "Horario para la línea $busLine"
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AdBanner(context = LocalContext.current)
    }
}