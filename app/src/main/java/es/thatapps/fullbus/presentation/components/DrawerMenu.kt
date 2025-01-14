package es.thatapps.fullbus.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import es.thatapps.fullbus.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DrawerMenu {
    @Composable
    fun Show(
        drawerState: DrawerState,
        navigationToRegister: () -> Unit,
        navigationToSettings: () -> Unit,
        content: @Composable () -> Unit
    ) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                DrawerContent(
                    navigationToRegister = navigationToRegister,
                    navigationToSettings = navigationToSettings
                )
            },
            content = content // Contenido principal de la pantalla
        )
    }

    @Composable
    private fun DrawerContent(
        navigationToRegister: () -> Unit,
        navigationToSettings: () -> Unit
    ) {
        Column (
            modifier = Modifier
                .widthIn(min = 300.dp)
                .fillMaxHeight()
                .background(Color.White)
                .padding(24.dp)
        ) {
            // Imagen de la parte superior
            Image(
                painter = painterResource(id = R.drawable.logo_fullbus),
                contentDescription = "Logo de la aplicacion",
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Opciones del menú
            MenuItem(
                iconRes = R.drawable.ic_logout,
                text = "Cerrar sesión",
                onClick = navigationToRegister
            )
        }
    }

    fun openMenu(scope: CoroutineScope, drawerState: DrawerState) {
        scope.launch { drawerState.open() }
    }
}