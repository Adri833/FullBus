package es.thatapps.fullbus.presentation.profile

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import es.thatapps.fullbus.presentation.components.adjustForMobile
import es.thatapps.fullbus.utils.ImageBase64
import es.thatapps.fullbus.utils.encodeImageToBase64
import es.thatapps.fullbus.utils.imagePickerLauncher
import es.thatapps.fullbus.utils.getPFP

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

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
    Column(
        modifier = Modifier
            .adjustForMobile()
            .fillMaxSize()
    ) {
        // Mostrar la imagen de perfil actual
        var pfp by remember { mutableStateOf<String?>(null) }

        LaunchedEffect(Unit) {
            pfp = getPFP()
        }

        Box(
            modifier = Modifier
                .padding(16.dp)
                .clickable {
                    imagePicker.launch("image/*")
                }
                .align(Alignment.CenterHorizontally)
        ) {
            // Imagen de perfil actual
            ImageBase64(
                imageUrl = pfp,
                size = 100.dp
            )
        }
    }
}