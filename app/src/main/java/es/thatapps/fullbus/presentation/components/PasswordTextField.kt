package es.thatapps.fullbus.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import es.thatapps.fullbus.R

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit
) {
    val passwordVisible = remember { mutableStateOf(false) }

    TextField(
        value = value,
        onValueChange = { onValueChange(it) },
        singleLine = true,
        label = {
            Text(text = "Contraseña")
        },

        // Icono para ver o dejar de ver la contraseña
        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        trailingIcon = {
            IconButton(
                onClick = { passwordVisible.value = !passwordVisible.value },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                val iconResID = if (passwordVisible.value) R.drawable.ojo_abierto else R.drawable.ojo_cerrao
                Image( // Propiedades de la foto
                    painter = painterResource(id = iconResID),
                    contentDescription = null,
                    modifier = Modifier.size(25.dp)
                )
            }
        }
    )
}