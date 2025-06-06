package es.thatapps.fullbus.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import es.thatapps.fullbus.R
import es.thatapps.fullbus.ui.theme.LightGray
import es.thatapps.fullbus.utils.ImageBase64
import es.thatapps.fullbus.utils.getPFP


@Composable
fun Header(
    onMenuClick: () -> Unit,
    navigationToProfile: () -> Unit,
) {
    val pfp = remember { mutableStateOf<String?>(null) }

    // Ejecutar la consulta a Firebase
    LaunchedEffect(Unit) {
        pfp.value = getPFP()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LightGray)
            .padding(
                start = 8.dp,
                end = 8.dp,
                top = 20.dp,
                bottom = 8.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Botón de menú
        IconButton(onClick = onMenuClick) {
            Icon(
                painter = painterResource(id = R.drawable.ic_menu),
                modifier = Modifier.width(30.dp),
                contentDescription = "Menu",
                tint = Color.Black
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


        Box (
            modifier = Modifier
                .border(2.dp, Color.Black, RoundedCornerShape(64.dp))
                .clickable {
                    navigationToProfile()
                }
        ) {
            // Imagen de perfil actual
            ImageBase64(
                imageUrl = pfp.value,
                size = 50.dp
            )
        }
    }
}