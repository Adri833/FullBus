package es.thatapps.fullbus.presentation.profile

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import es.thatapps.fullbus.presentation.components.DrawerMenu
import es.thatapps.fullbus.presentation.components.Header
import es.thatapps.fullbus.utils.ImageBase64
import es.thatapps.fullbus.utils.encodeImageToBase64
import es.thatapps.fullbus.utils.imagePickerLauncher
import es.thatapps.fullbus.utils.getPFP
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navigationToSettings: () -> Unit,
    navigationToLogin: () -> Unit,
    navigationToProfile: () -> Unit,
    navigationToHome: () -> Unit
) {
    val context = LocalContext.current
    val drawerState = rememberDrawerState(DrawerValue.Closed) // Estados del menú
    val scope = rememberCoroutineScope()
    val drawerMenu = DrawerMenu()

    // Lector de imágenes (Selector de imágenes)
    val imagePicker = imagePickerLauncher { uri: Uri? ->
        uri?.let {
            // Convertimos la imagen seleccionada a Base64
            val base64Image = encodeImageToBase64(context, it)

            base64Image?.let { encodedImage ->
                // Subimos la imagen codificada a Firestore
                viewModel.updatePFP(encodedImage)
            } ?: run {
                Toast.makeText(context, "Error al convertir la imagen", Toast.LENGTH_SHORT).show()
            }
        }
    }

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
        }
    ) {
        // Header
        Header(onMenuClick = { scope.launch { drawerState.open() } })

        // Mostrar la imagen de perfil actual
        var pfp by remember { mutableStateOf<String?>(null) }

        LaunchedEffect(Unit) {
            pfp = getPFP()
        }

        Column(modifier = Modifier.padding(16.dp)) {
            // Imagen de perfil actual
            pfp?.let { imageUrl ->
                ImageBase64(imageUrl = imageUrl, width = 100.dp, height = 100.dp)
            } ?: run {
                Text(text = "No tienes foto de perfil")
            }

            // Botón para seleccionar y cambiar la imagen de perfil
            Button(
                onClick = {
                    imagePicker.launch("image/*") // Abrimos el selector de imágenes
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Seleccionar foto de perfil")
            }
        }
    }
}