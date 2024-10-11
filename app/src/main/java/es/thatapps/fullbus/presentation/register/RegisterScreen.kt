package es.thatapps.fullbus.presentation.register

import android.widget.Toast
import androidx.compose.animation.core.estimateAnimationDurationMillis
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import es.thatapps.fullbus.R
import es.thatapps.fullbus.presentation.components.PasswordTextField
import es.thatapps.fullbus.presentation.components.RegisterTextField


@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    navigationToLogin: () -> Unit,
) {
    // Estados para almacenar los valores de entrada
    val username = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val registerState by viewModel.registerState.collectAsState()

    // Obtener el controlador del teclado
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo_fullbus),
            contentDescription = "Logo de la aplicacion",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "Ingresa tus datos", fontSize = 28.sp, color = Color.Black)

        Spacer(modifier = Modifier.height(20.dp))

        RegisterTextField(
            value = username.value,
            placeHolder = "Nombre de Usuario"
        ) {
            username.value = it // Actualiza el valor del nombre
        }
        Spacer(modifier = Modifier.height(11.dp))

        RegisterTextField(
            value = email.value,
            placeHolder = "Email"
        ) {
            email.value = it // Actualiza el valor del email
        }
        Spacer(modifier = Modifier.height(11.dp))

        PasswordTextField(
            value = password.value,
            onValueChange = {
                password.value = it // Actualiza el valor de la contraseña
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                // Oculta el teclado
                keyboardController?.hide()

                viewModel.register(email.value, password.value, username.value) // Llama a la función de registro
            },
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Registrarse", fontSize = 17.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(10.dp))

        val context = LocalContext.current

        // Muestra los estados al registrarse
        when (registerState) {
            is RegisterState.Loading -> CircularProgressIndicator() // Circulo mientras carga
            is RegisterState.Success -> {
                viewModel.resetRegisterState() // Reestablece el estado para evitar un bucle
                Toast.makeText(context, "Registro exitoso!", Toast.LENGTH_SHORT).show() // Notificacion de exito
                navigationToLogin()
            }
            is RegisterState.Error -> {
                val errorState = registerState as RegisterState.Error
                Text(
                    text = stringResource(id = errorState.messageResID),
                    color = Color.Black
                )
            }
            else -> {}
        }

        // Espacio en blanco hasta la parte inferior de la pantalla
        Spacer(modifier = Modifier.weight(1f))

        Text(text = "¿Ya tienes cuenta?", fontSize = 17.sp, color = Color.Black)

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = navigationToLogin,
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Iniciar Sesión", fontSize = 17.sp, color = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(
        navigationToLogin = {}
    )
}