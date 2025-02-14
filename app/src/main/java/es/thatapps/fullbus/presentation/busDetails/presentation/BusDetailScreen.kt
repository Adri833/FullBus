package es.thatapps.fullbus.presentation.busDetails.presentation

import android.annotation.SuppressLint
import android.provider.Settings.Global
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import es.thatapps.fullbus.R
import es.thatapps.fullbus.data.repository.AuthRepository
import es.thatapps.fullbus.presentation.components.AdBanner
import es.thatapps.fullbus.presentation.components.BackButton
import es.thatapps.fullbus.presentation.components.DrawerMenu
import es.thatapps.fullbus.presentation.components.Header
import es.thatapps.fullbus.presentation.components.HorizontalPagerBuses
import es.thatapps.fullbus.presentation.components.adjustForMobile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@SuppressLint("DiscouragedApi")
@Composable
fun BusDetailScreen(
    busLine: String,
    navigationToLogin: () -> Unit,
    navigationToSettings: () -> Unit,
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

                    BackButton(navController)

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp)
                    ) {
                        HorizontalPagerBuses(vueltaBuses, viewModel)
                    }
                }

                2 -> { // Horario

                    BackButton(navController)

                    Box(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        // Formatea el nombre del recurso
                        val resourceName = "horario_${busLine.replace("-", "").lowercase()}"
                        val resourceId = LocalContext.current.resources.getIdentifier(
                            resourceName,
                            "drawable",
                            LocalContext.current.packageName
                        )

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
}