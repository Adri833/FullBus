package es.thatapps.fullbus.presentation.profile

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import es.thatapps.fullbus.presentation.components.BackButton
import es.thatapps.fullbus.presentation.components.adjustForMobile
import es.thatapps.fullbus.utils.ImageBase64
import es.thatapps.fullbus.utils.encodeImageToBase64
import es.thatapps.fullbus.utils.imagePickerLauncher
import es.thatapps.fullbus.utils.getPFP

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    var pfp by remember { mutableStateOf<String?>(null) }
    var username by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }
    var newUsername by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        pfp = getPFP()
        username = viewModel.getUserName()
        newUsername = username
    }

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
            .padding(top = 50.dp, start = 16.dp, end = 16.dp),
    ) {
        // Boton para volver atras
        BackButton(navController)

        // Foto de perfil clicable para cambiarla
        Box(
            modifier = Modifier
                .border(4.dp, Color.Black, RoundedCornerShape(72.dp))
                .clickable {
                    imagePicker.launch("image/*")
                }
                .align(Alignment.CenterHorizontally)
        ) {
            // Imagen de perfil actual
            ImageBase64(
                imageUrl = pfp,
                size = 140.dp
            )

            // Icono de lapiz
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(2.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar foto",
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            // Nombre de usuario editable
            if (isEditing) {
                TextField(
                    value = newUsername,
                    onValueChange = { newUsername = it },
                    modifier = Modifier
                        .focusRequester(focusRequester),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            // Guardar el nuevo nombre de usuario
                            viewModel.updateUserName(newUsername)
                            username = newUsername
                            isEditing = false
                            focusManager.clearFocus()
                        }
                    ),
                    singleLine = true,
                )
            } else {
                Text(
                    text = username,
                    modifier = Modifier
                        .clickable { isEditing = true },
                    fontSize = 35.sp,
                    color = Color.Black
                )
            }
        }
    }
}