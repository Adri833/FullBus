package es.thatapps.fullbus.presentation.loading

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import es.thatapps.fullbus.R

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        // Intercepta los toques en pantalla y evita que se pase a la capa inferior
                        awaitPointerEvent()
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.Black.copy(alpha = 0.2f),
            modifier = Modifier
                .wrapContentSize()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier
                        .size(80.dp),
                    painter = painterResource(id = R.drawable.logo_fullbus),
                    contentDescription = "Logo FullBus",
                    contentScale = ContentScale.Crop,
                )
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier
                        .size(80.dp)
                )
            }
        }
    }
}