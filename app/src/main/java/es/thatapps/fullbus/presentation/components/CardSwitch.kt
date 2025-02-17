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
fun CardSwitch(
    title: String,
    isCheked: Boolean,
    onChekedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ){ Text(
            text = title,
            fontSize = 18.sp,
            modifier = Modifier.weight(1f)
        )
            Switch(
                checked = isCheked,
                onCheckedChange = onChekedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White, // interruptor activo
                    uncheckedThumbColor = Color(0xFFBF0000), // interruptor inactivo
                    checkedTrackColor = Color(0xFFD32F2F), // pista activa
                    uncheckedTrackColor = Color(0xFFE0E0E0) // pista inactiva
                )
            )
        }
    }
}
