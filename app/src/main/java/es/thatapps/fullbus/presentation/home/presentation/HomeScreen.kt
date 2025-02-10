package es.thatapps.fullbus.presentation.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import es.thatapps.fullbus.presentation.components.AdBanner
import es.thatapps.fullbus.presentation.components.BusInfoBox
import es.thatapps.fullbus.presentation.components.DrawerMenu
import es.thatapps.fullbus.presentation.components.Header
import es.thatapps.fullbus.presentation.components.adjustForMobile
import es.thatapps.fullbus.presentation.home.domain.BusLineMockData
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onBusLineClick: (String) -> Unit, // Función para manejar clics en líneas de autobús
    navigationToLogin: () -> Unit,
    navigationToSettings: () -> Unit,
    navigationToProfile: () -> Unit,
    navigationToHome: () -> Unit
) {

    // Estados del menu
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val drawerMenu = DrawerMenu()

    // Estructura principal con el menú lateral y el header
    drawerMenu.Show(
        drawerState = drawerState,
        navigationToLogin = navigationToLogin,
        navigationToSettings = navigationToSettings,
        navigationToProfile = navigationToProfile,
        navigationToHome = navigationToHome,
        onLogout = {
            viewModel.logout()
            navigationToLogin()
        },
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .adjustForMobile()
        ) {
            // Header sin padding para que ocupe toda la pantalla horizontalmente
            Header(onMenuClick = { scope.launch { drawerState.open() } }, navigationToProfile)

            Spacer(modifier = Modifier.height(15.dp))

            // Contenido principal de la página con padding
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyColumn(
                    // Se ancla entre el Header y el AdBanner
                    modifier = Modifier.weight(1f),
                    content = {
                        itemsIndexed(BusLineMockData.getMockBusLines()) { _, item ->
                            item.apply {
                                BusInfoBox(
                                    lineNumber = line,
                                    backgroundColor1 = color.colorBase,
                                    backgroundColor2 = color.colorAccent,
                                    origin = origin,
                                    destination = destination,
                                ) {
                                    // Recoge la linea de bus en la que hacemos clic
                                    onBusLineClick(line)
                                }
                            }
                        }
                    }
                )

                // Espacio en blanco hasta la parte inferior de la pantalla
                Spacer(modifier = Modifier.height(16.dp))

                //Banner de anuncio en la parte inferior
                AdBanner(context = LocalContext.current)
            }
        }
    }
}