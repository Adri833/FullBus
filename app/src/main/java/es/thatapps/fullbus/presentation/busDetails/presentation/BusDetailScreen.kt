package es.thatapps.fullbus.presentation.busDetails.presentation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import es.thatapps.fullbus.presentation.components.AdBanner
import es.thatapps.fullbus.presentation.components.BackButton
import es.thatapps.fullbus.presentation.components.DrawerMenu
import es.thatapps.fullbus.presentation.components.Header
import es.thatapps.fullbus.presentation.components.HorizontalPagerBuses
import es.thatapps.fullbus.presentation.components.NoBusesAvailableMessage
import es.thatapps.fullbus.presentation.components.adjustForMobile
import es.thatapps.fullbus.presentation.loading.LoadingScreen
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.TimeZone

@SuppressLint("DiscouragedApi")
@Composable
fun BusDetailScreen(
    busLine: String,
    navigationToLogin: () -> Unit,
    navigationToHome: () -> Unit,
    navigationToProfile : () -> Unit,
    viewModel: BusViewModel = hiltViewModel(),
    navController: NavController
) {
    // Observar el estado de los buses activos
    val activeBuses by viewModel.activeBuses.collectAsState()
    val filteredBuses = activeBuses.filter { it.line == busLine }
    val idaBuses = filteredBuses.filter { it.direction == "Ida" }
    val vueltaBuses = filteredBuses.filter { it.direction == "Vuelta" }
    val isLoading by viewModel.isLoading.collectAsState()
    val season = when (Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid")).get(Calendar.MONTH)) {
        Calendar.JULY, Calendar.AUGUST -> "summer"
        else -> "winter"
    }

    // Estados del menu
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val drawerMenu = DrawerMenu()

    // Estructura principal con el menú lateral y el header
    drawerMenu.Show(
        drawerState = drawerState,
        navigationToLogin = navigationToLogin,
        navigationToProfile = navigationToProfile,
        navigationToHome = navigationToHome,
        onLogout = {
            viewModel.logout()
            navigationToLogin()
        }
    ) {
        // Actualiza la hora cada segundo
        LaunchedEffect(activeBuses) {
            Log.d("BusDetailScreen", "Buses activos actualizados: $activeBuses")
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
            Header(onMenuClick = { scope.launch { drawerState.open() } }, navigationToProfile)

            if (isLoading) {
                LoadingScreen()
            }

            // Navegacion superior
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(text = title) }
                    )
                }
            }

            // Contenido dinámico según la pestaña seleccionada
            when (selectedTabIndex) {
                0 -> { // Ida

                    BackButton(navController)

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp)
                    ) {
                        if (idaBuses.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                NoBusesAvailableMessage(busLine)
                            }

                        }
                        HorizontalPagerBuses(idaBuses, viewModel)
                    }
                }

                1 -> { // Vuelta

                    BackButton(navController)

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp)
                    ) {
                        if (vueltaBuses.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                NoBusesAvailableMessage(busLine)
                            }

                        }
                        HorizontalPagerBuses(vueltaBuses, viewModel)
                    }
                }

                2 -> { // Horario

                    BackButton(navController)

                    Box(
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp, top = 16.dp)
                            .weight(1f)
                    ) {
                        // Formatea el nombre del recurso
                        val resourceName = "horario_${busLine.replace("-", "").lowercase()}_$season"
                        val resourceId = LocalContext.current.resources.getIdentifier(
                            resourceName,
                            "drawable",
                            LocalContext.current.packageName
                        )

                        // Muestra la imagen si existe
                        if (resourceId != 0) {
                            Box(
                                modifier = Modifier
                                    .border(BorderStroke(6.dp, Color.Black), shape = RoundedCornerShape(6.dp))
                                    .padding(4.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = resourceId),
                                    contentDescription = "Horario para la línea $busLine"
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            AdBanner(context = LocalContext.current)
        }
    }
}