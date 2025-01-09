package es.thatapps.fullbus.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.thatapps.fullbus.R
import es.thatapps.fullbus.ui.theme.LightGray

@Composable
fun Header(
    navigationToRegister: () -> Unit,
    navigationToSettings: () -> Unit,
) {
    // Estado para controlar la visibilidad del menú desplegable
    var showMenu by remember { mutableStateOf(false) }
    // Estado para controlar la visibilidad del diálogo de sugerencias
    var showSuggestionDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                LightGray, // Color de fondo gris claro
            )
            .padding(
                start = 8.dp,
                end = 8.dp,
                top = 20.dp,
                bottom = 8.dp
            ), // Espacios para ayudar al centrado
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Botón de menú
        IconButton(onClick = { showMenu = !showMenu }) { // Abre el menú desplegable al hacer click
            Icon(
                painter = painterResource(id = R.drawable.ic_menu),
                modifier = Modifier.width(30.dp),
                contentDescription = "Menu",
                tint = Color.Black
            )
        }

        // Menú desplegable
        DropdownMenu(
            expanded = showMenu,
            containerColor = Color(0xFFB0BEC5), // Color de fondo del menú
            onDismissRequest = { showMenu = false } // Se oculta al clickar fuera de él
        ) {
            // Elemento para la configuracion
            DropdownMenuItem(
                text = {
                    Text("Configuración",
                        fontSize = 16.sp,
                        color = Color.Black)
                       },
                onClick = {
                    showMenu = false
                    navigationToSettings() // Navegar hacia la pantalla de configuracion
                },
                modifier = Modifier.padding(8.dp)
            )

            // Elemento para la configuración de perfil
            DropdownMenuItem(
                text = {
                    Text("Perfil",
                        fontSize = 16.sp,
                        color = Color.Black)
                       },
                onClick = {
                    // TODO : logica de la configuración de perfil
                    showMenu = false
                },
                modifier = Modifier.padding(8.dp)
            )

            // Elemento para enviar sugerencias
            DropdownMenuItem(
                text = {
                    Text("Sugerencias",
                        fontSize = 16.sp,
                        color = Color.Black)
                       },
                onClick = {
                    showMenu = false
                    showSuggestionDialog = true // Muestra la pantalla de sugerencias
                },
                modifier = Modifier.padding(8.dp)
            )

            // Elemento para cerrar sesion
            DropdownMenuItem(
                text = {
                    Text("Cerrar sesión",
                    fontSize = 16.sp,
                    color = Color.Black)
                       },
                onClick = {
                    showMenu = false
                    navigationToRegister() // Navegar a la pantalla de registro
                },
                modifier = Modifier.padding(8.dp)
            )

        }

        Spacer(modifier = Modifier.weight(1f))

        // Logo de la aplicación en el centro
        Image(
            painter = painterResource(id = R.drawable.logo_fullbus2),
            contentDescription = "Logo FullBus",
            modifier = Modifier.size(90.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        // Botón de perfil de usuario
        IconButton(onClick = { /* Acción para el perfil */ }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_avatar),
                contentDescription = "Profile",
                modifier = Modifier.width(40.dp),
                tint = Color.Black
            )
        }
    }

    // Mostrar el cuadro de sugerencias si el estado es verdadero
    if (showSuggestionDialog) {
        SuggestionDialog(
            onDismiss = { showSuggestionDialog = false },
            onSend = { _ ->
                // TODO: logica de la sugerencia (email, mensajitos...)
                showSuggestionDialog = false // Cierra el diálogo después de enviar
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHeader() {
    Header(
        navigationToRegister = { /* Acción de navegación a registrar */ },
        navigationToSettings = { /* Acción de navegación a configuraciones */ }
    )
}