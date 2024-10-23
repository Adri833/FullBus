package es.thatapps.fullbus.presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.thatapps.fullbus.presentation.components.SettingItem

@Composable
fun SettingsScreen(
    isDarkModeEnabled: Boolean,
    onDarkModeToggle: (Boolean) -> Unit,
    areNotificationsEnabled: Boolean,
    onNotificationToggle: (Boolean) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp), // Espaciado entre items
        horizontalAlignment = Alignment.CenterHorizontally // Centra el contenido horizontalmente
    ) {
        // Encabezado
        Text(
            text = "Configuración",
            style = MaterialTheme.typography.displayMedium.copy(
                fontSize = 28.sp
            ),

            modifier = Modifier.padding(bottom = 24.dp) // Más espacio debajo del título
        )

        // Item Modo Oscuro en una Card para un diseño más limpio
        SettingItem(
            title = "Modo Oscuro",
            isCheked = isDarkModeEnabled,
            onChekedChange = onDarkModeToggle,
            modifier = Modifier.padding(16.dp) // Relleno dentro del card
        )

        // Item Notificaciones en una Card
        SettingItem(
            title = "Activar notificaciones",
            isCheked = areNotificationsEnabled,
            onChekedChange = onNotificationToggle,
            modifier = Modifier.padding(16.dp)
        )
    }
}
