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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import es.thatapps.fullbus.R

class DrawerMenu {
    @Composable
    fun Show(
        drawerState: DrawerState,
        navigationToLogin: () -> Unit,
        navigationToProfile: () -> Unit,
        navigationToHome: () -> Unit,
        onLogout : () -> Unit,
        content: @Composable () -> Unit
    ) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                DrawerContent(
                    navigationToLogin = navigationToLogin,
                    navigationToProfile = navigationToProfile,
                    navigationToHome = navigationToHome,
                    onLogout = onLogout
                )
            },
            content = content // Contenido principal de la pantalla
        )
    }

    @Composable
    private fun DrawerContent(
        navigationToLogin: () -> Unit,
        navigationToProfile: () -> Unit,
        navigationToHome: () -> Unit,
        onLogout : () -> Unit,
    ) {
        Column (
            modifier = Modifier
                .width(300.dp)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.background)
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
                iconRes = R.drawable.ic_home,
                text = "Inicio",
                onClick =  navigationToHome
            )

            MenuItem(
                iconRes = R.drawable.ic_profile,
                text = "Perfil",
                onClick = navigationToProfile
            )

            SuggestionMenuItem()

            MenuItem(
                iconRes = R.drawable.ic_logout,
                text = "Cerrar sesión",
                onClick = {
                    onLogout()
                }
            )
        }
    }

    @Composable
    fun SuggestionMenuItem() {
        val showDialog = remember { mutableStateOf(false) }

        // MenuItem que abre el cuadro de sugerencias
        MenuItem(
            iconRes = R.drawable.ic_suggest,
            text = "Sugerencias",
            onClick = {
                showDialog.value = true
            }
        )

        // Mostrar el cuadro de diálogo si el estado es verdadero
        if (showDialog.value) {
            SuggestionDialog(
                onDismiss = { showDialog.value = false },
                onSend = { suggestion ->
                    showDialog.value = false
                }
            )
        }
    }
}