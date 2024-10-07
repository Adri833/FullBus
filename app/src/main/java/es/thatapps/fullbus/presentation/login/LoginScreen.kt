package es.thatapps.fullbus.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun LoginScreen() {
    Column(
        modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
        .padding(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Tu madre tiene una polla", fontSize = 25.sp, color = Color.Black)
        Text("Que ya la quisiera yo", fontSize = 25.sp, color = Color.Black)
    }
}
