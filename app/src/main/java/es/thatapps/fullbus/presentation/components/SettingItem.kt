package es.thatapps.fullbus.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingItem(
    title: String,
    isCheked: Boolean,
    onChekedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier // Para que sea personalizable
) {
    Card(
        shape = RoundedCornerShape(12.dp), // Bordes redondeados
        elevation = CardDefaults.cardElevation(8.dp), // Elevación para sombra
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){ Text(
            text = title,
            fontSize = 18.sp,
            modifier = Modifier.weight(1f) // Deja que el texto ocupe todo el espacio disponible
        )
            Switch(
                checked = isCheked,
                onCheckedChange = onChekedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFF6200EE), // Color del interruptor
                    uncheckedThumbColor = Color(0xFFB0BEC5)
                )
            )
        }
    }
}
