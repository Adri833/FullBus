package es.thatapps.fullbus.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
        navigationToProfile: () -> Unit,
        content: @Composable () -> Unit
    ) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                DrawerContent(
                    navigationToRegister = navigationToRegister,
                    navigationToSettings = navigationToSettings,
                    navigationToProfile = navigationToProfile
                )
            },
            content = content // Contenido principal de la pantalla
        )
    }

    @Composable
    private fun DrawerContent(
        navigationToRegister: () -> Unit,
        navigationToSettings: () -> Unit,
        navigationToProfile: () -> Unit
    ) {
        Column (
            modifier = Modifier
                .width(300.dp)
                .fillMaxHeight()
                .background(Color.White)
        ) {
            // Imagen de la parte superior
            Box(
                modifier = Modifier
                    .width(300.dp)
                    .height(150.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.fondo_bus),
                    contentDescription = "Fondo de la parte superior",
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Opciones del menú
            MenuItem(
                iconRes = R.drawable.ic_profile,
                text = "Perfil",
                onClick = navigationToProfile
            )

            MenuItem(
                iconRes = R.drawable.ic_settings,
                text = "Ajustes",
                onClick = navigationToSettings
            )

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