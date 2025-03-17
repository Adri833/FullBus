package es.thatapps.fullbus.presentation.profile

import es.thatapps.fullbus.presentation.components.ConfirmationDialog
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import es.thatapps.fullbus.presentation.components.AdBanner
import es.thatapps.fullbus.presentation.components.BackButton
import es.thatapps.fullbus.presentation.components.CardClickable
import es.thatapps.fullbus.presentation.components.CardInfo
import es.thatapps.fullbus.presentation.components.adjustForMobile
import es.thatapps.fullbus.utils.ImageBase64
import es.thatapps.fullbus.utils.encodeImageToBase64
import es.thatapps.fullbus.utils.imagePickerLauncher

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val pfp by viewModel.pfp.collectAsState()
    var username by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }
    var newUsername by remember { mutableStateOf("") }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        viewModel.loadPFP()
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
                viewModel.loadPFP()
            } ?: run {
                Toast.makeText(context, "Error al convertir la imagen", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .adjustForMobile()
            .padding(top = 50.dp, start = 16.dp, end = 16.dp),
    ) {
        // Boton para volver atras
        BackButton(navController)

        // Texto de perfil
        Text(
            text = "Perfil",
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            fontSize = 36.sp,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Foto de perfil clicable para cambiarla
        Box(
            modifier = Modifier
                .border(4.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(72.dp))
                .clickable {
                    imagePicker.launch("image/*")
                }
                .align(Alignment.CenterHorizontally)
        ) {
            // Imagen de perfil actual
            ImageBase64(
                imageUrl = pfp,
                size = 130.dp
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
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            // Nombre de usuario editable
            if (isEditing) {
                TextField(
                    value = newUsername,
                    onValueChange = {
                        if (it.length <= 12) {
                            newUsername = it
                        }
                    },
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .border(0.dp, Color.Transparent),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (newUsername.isNotBlank()) { // Evita guardar un nombre vacio
                                viewModel.updateUserName(newUsername)
                                username = newUsername
                                isEditing = false
                                focusManager.clearFocus()
                            } else {
                                Toast.makeText(
                                    context,
                                    "El nombre de usuario no puede estar vacío",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    ),
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 30.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent
                    )
                )
            } else {
                Text(
                    text = username,
                    modifier = Modifier
                        .clickable { isEditing = true },
                    fontSize = 30.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Barra horizontal
        HorizontalDivider(
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 16.dp, bottom = 32.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Card para el correo electronico
        CardInfo(
            content = viewModel.getEmail().toString(),
            icon = Icons.Default.MailOutline,
        )

        Spacer(modifier = Modifier.height(20.dp))

        CardClickable(
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            onClick = { showLogoutDialog = true },
            title = "Cerrar Sesión",
        )

        Spacer(modifier = Modifier.height(20.dp))

        CardClickable(
            icon = Icons.Default.Delete,
            onClick = { showDeleteDialog = true },
            title = "Borrar cuenta",
        )

        Spacer(Modifier.weight(1f))

        AdBanner(context)
    }

    // Mensaje de confirmacion de cierre de sesion
    ConfirmationDialog(
        showDialog = showLogoutDialog,
        title = "Cerrar Sesión",
        message = "¿Seguro que quieres cerrar sesión?",
        confirmText = "Cerrar Sesión",
        onConfirm = {
            viewModel.logout(navController)
            showLogoutDialog = false
        },
        onDismiss = { showLogoutDialog = false }
    )

    // Mensaje de confirmacion de borrado de cuenta
    ConfirmationDialog(
        showDialog = showDeleteDialog,
        title = "Borrar cuenta",
        message = "¿Seguro que quieres borrar tu cuenta?",
        confirmText = "Borrar",
        onConfirm = {
            viewModel.deleteAccount(navController) {
                Toast.makeText(context, "Error al borrar la cuenta", Toast.LENGTH_SHORT).show()
            }
            showDeleteDialog = false
        },
        onDismiss = { showDeleteDialog = false }
    )
}