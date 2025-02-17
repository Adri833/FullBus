package es.thatapps.fullbus.presentation.register

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import es.thatapps.fullbus.R
import es.thatapps.fullbus.presentation.components.PasswordTextField
import es.thatapps.fullbus.presentation.components.adjustForMobile
import es.thatapps.fullbus.presentation.loading.LoadingScreen
import es.thatapps.fullbus.utils.AsyncResult

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    navigationToLogin: () -> Unit,
    navigationToHome: () -> Unit,
) {

    // UI
    RegisterView(viewModel, navigationToLogin)

    // Muestra los estados al registrarse
    val asyncResult by viewModel.registerState.collectAsState()
    val context = LocalContext.current
    when (asyncResult) {
        is AsyncResult.Loading -> LoadingScreen() // Circulo mientras carga
        is AsyncResult.Success -> {
            viewModel.resetRegisterState() // Reestablece el estado para evitar un bucle
            Toast.makeText(context, "Registro exitoso!", Toast.LENGTH_SHORT)
                .show() // Notificacion de exito
            navigationToHome()
        }
        else -> {}
    }
}

@Composable
fun RegisterView(
    viewModel: RegisterViewModel,
    navigationToLogin: () -> Unit,
) {
    // Estados para almacenar los valores de entrada
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val asyncResult by viewModel.registerState.collectAsState()

    // Obtener el controlador del teclado
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .adjustForMobile()
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 70.dp, start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_fullbus),
            contentDescription = "Logo de la aplicacion",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Text(
            text = "Registrarse",
            fontSize = 28.sp,
            color = Color.Black,
            modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
        )

        // Campo de texto para el email
        TextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        PasswordTextField(
            value = password.value,
            onValueChange = {
                password.value = it
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Mostrar mensaje de error en un recuadro rojo si hay algún error
        if (asyncResult is AsyncResult.Error) {
            val errorMessage = when (val msg = (asyncResult as AsyncResult.Error).message) {
                is Int -> context.getString(msg)
                is String -> msg
                else -> "Error desconocido"
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Red, shape = RoundedCornerShape(4.dp))
                    .background(Color(0xFFFFD2D7), shape = RoundedCornerShape(4.dp))
                    .padding(8.dp),
                contentAlignment = Alignment.Center // Centrar el texto
            ) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                // Oculta el teclado
                keyboardController?.hide()

                viewModel.register(email.value, password.value)
            },
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Registrarse", fontSize = 17.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Enlace a la pantalla de inicio sesion
        TextButton(
            onClick = { navigationToLogin() },
        ) {
            Text(
                buildAnnotatedString {
                    append("¿Ya tienes cuenta? ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Iniciar sesion")
                    }
                }
            )
        }
    }
}