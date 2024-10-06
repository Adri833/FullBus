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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.thatapps.fullbus.presentation.components.PasswordTextField
import es.thatapps.fullbus.presentation.components.RegisterTextField

@Preview
@Composable
fun RegisterScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Ingresa tus datos", fontSize = 28.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Necesitamos estos datos para \n poder crear tu cuenta de usuario",
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        RegisterTextField(
            value = "userName",
            placeHolder = "Nombre"
        ) {
            // TODO agregar logica
        }
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
                // TODO agregar logica
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