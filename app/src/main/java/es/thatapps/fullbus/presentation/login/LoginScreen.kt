package es.thatapps.fullbus.presentation.login

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import es.thatapps.fullbus.R


@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    navigationToRegister: () -> Unit,
    navigationToMain: () -> Unit,
) {
    // Estados para almacenar los valores de entrada
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }
    val loginState by viewModel.loginState.collectAsState()

    // Obtener el controlador del teclado
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(25.dp))

        Image(
            painter = painterResource(id = R.drawable.logo_fullbus),
            contentDescription = "Logo de la aplicacion",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Iniciar Sesión",
            fontSize = 28.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Campo de texto para el email
        TextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,

            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )
        Spacer(modifier = Modifier.height(11.dp))

        // Campo de texto para la contraseña
        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Contraseña") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),

            // Icono para ver o dejar de ver la contraseña
            visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(
                    onClick = { passwordVisible.value = !passwordVisible.value },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    val iconResID = if (passwordVisible.value) R.drawable.ojo_abierto else R.drawable.ojo_cerrao
                    Image( // Propiedades de la foto
                        painter = painterResource(id = iconResID), contentDescription = null, modifier = Modifier.size(25.dp))
                }
            }
        )
        Spacer(modifier =  Modifier.height(23.dp))

        // Mostrar mensaje de error en un recuadro rojo si hay algún error
        if (loginState is LoginState.Error) {
            val errorState = loginState as LoginState.Error
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Red, shape = RoundedCornerShape(4.dp)) // Borde de color rojo
                    .background(Color(0xFFFFD2D7), shape = RoundedCornerShape(4.dp)) // Color rojo más claro
                    .padding(8.dp),
                contentAlignment = Alignment.Center // Centrar el texto
            ) {
                Text(
                    text = stringResource(id = errorState.messageResID),
                    color = Color.Red,
                    fontSize = 15.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Botón de inicio de sesión
        Button(
            onClick = {
                // Oculta el teclado
                keyboardController?.hide()

                viewModel.login(email.value, password.value) // Llama a la función de login
            },
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Iniciar Sesión", fontSize = 17.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(10.dp))

        val context = LocalContext.current

        // Muestra los estados al registrarse
        when (loginState) {
            is LoginState.Loading -> CircularProgressIndicator() // Circulo mientras carga
            is LoginState.Success -> {
                viewModel.resetLoginState() // Reestablece el estado para evitar un bucle
                Toast.makeText(context, "Bienvenido", Toast.LENGTH_SHORT).show() // Notificacion de exito
                navigationToMain()
            }
            else -> {}
        }

        // Enlace a la pantalla de registro
        TextButton(
            onClick = { navigationToRegister() },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("¿No tienes cuenta? Regístrate")
        }

        TextButton(
            onClick = { viewModel.resetPassword(email.value) }, // Llama a la función de restablecimiento de contraseña
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("¿Olvidaste tu contraseña?")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Recuadro verde para mostrar si el correo de recuperacion ha sido enviado
        if (loginState is LoginState.PasswordResetSuccess) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Green, shape = RoundedCornerShape(4.dp)) // Borde de color rojo
                    .background(Color(0xFFD0E8D0), shape = RoundedCornerShape(4.dp)) // Color rojo más claro
                    .padding(8.dp),
                contentAlignment = Alignment.Center // Centrar el texto
            ) {
                Text(
                    text = stringResource(R.string.email_send),
                    color = Color(0xFF004D00),
                    fontSize = 15.sp
                )
            }
        }
    }
}