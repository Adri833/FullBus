package es.thatapps.fullbus.presentation.register

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import es.thatapps.fullbus.navigation.Routes
import es.thatapps.fullbus.presentation.components.PasswordTextField
import es.thatapps.fullbus.presentation.components.RegisterTextField

@Composable
fun RegisterScreen(navController: NavController, viewModel: RegisterViewModel = hiltViewModel()) {
    var userName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }
    var userPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Ingresa tus datos", fontSize = 28.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(20.dp))
        RegisterTextField(
            value = "",
            placeHolder = "Nombre",
            onValueChange = { userName = it }
        )

        Spacer(modifier = Modifier.height(10.dp))
        RegisterTextField(
            value = "userEmail",
            placeHolder = "Email"
        ) {
            // TODO agregar logica
        }
        Spacer(modifier = Modifier.height(10.dp))
        PasswordTextField(
            value = "userPassword",
            onValueChange = {
                // TODO agregar logica
            })
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                navController.navigate(Routes.Login.route)
            },
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Registrarse")
        }
    }
}

// Esta es la misma función de RegisterScreen pero usando un NavController falso para el preview
@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    val navController = rememberNavController() // NavController de prueba para el Preview
    RegisterScreen(navController)
}