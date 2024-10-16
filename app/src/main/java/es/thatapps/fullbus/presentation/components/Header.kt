package es.thatapps.fullbus.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.thatapps.fullbus.R

@Composable
fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFB0BEC5)) // Color de fondo gris claro
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Botón de menú
        IconButton(onClick = { /* Acción para abrir el menú */ }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_menu), // Asegúrate de tener este recurso
                contentDescription = "Menu",
                tint = Color.Black
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Logo de la aplicación en el centro
        Image(
            painter = painterResource(id = R.drawable.logo_fullbus2),
            contentDescription = "Logo FullBus",
            modifier = Modifier.size(60.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        // Botón de perfil de usuario
        IconButton(onClick = { /* Acción para el perfil */ }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_avatar), // Asegúrate de tener este recurso
                contentDescription = "Profile",
                tint = Color.Black
            )
        }
    }
}