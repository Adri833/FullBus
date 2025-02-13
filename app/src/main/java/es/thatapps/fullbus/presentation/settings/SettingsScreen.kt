package es.thatapps.fullbus.presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import es.thatapps.fullbus.presentation.components.CardSwitch

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val isDarkModeEnabled by viewModel.isDarkModeEnabled.collectAsState()
    val areNotificationsEnabled by viewModel.areNotificationsEnabled.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Encabezado
        Text(
            text = "Configuración",
            style = MaterialTheme.typography.displayMedium.copy(
                fontSize = 28.sp
            ),

            modifier = Modifier.padding(bottom = 24.dp) // Más espacio debajo del título
        )

        // Item Modo Oscuro
        CardSwitch(
            title = "Modo Oscuro",
            isCheked = isDarkModeEnabled,
            onChekedChange = { viewModel.toggleDarkMode(it) },
            modifier = Modifier.padding(16.dp)
        )

        // Item Notificaciones
        CardSwitch(
            title = "Activar notificaciones",
            isCheked = areNotificationsEnabled,
            onChekedChange = { viewModel.toggleNotifications(it) },
            modifier = Modifier.padding(16.dp)
        )
    }
}
