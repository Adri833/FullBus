package es.thatapps.fullbus.presentation.main

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.thatapps.fullbus.R
import es.thatapps.fullbus.presentation.components.AdBanner
import es.thatapps.fullbus.presentation.components.BusStatusBox

@Composable
fun MainScreen(context: Context) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Encabezado con la información de la línea de autobús y el logo
        HeaderSection()

        Spacer(modifier = Modifier.height(16.dp))

        // Contenedor de autobuses
        BusStatusBox(
            time = "7:00",
            isFull = false,
            onReportFull = { /* acción al marcar como lleno */ }
        )

        Spacer(modifier = Modifier.height(16.dp))

        BusStatusBox(
            time = "7:15",
            isFull = true,
            reportCount = 3,
            onReportFull = { /* acción al marcar como lleno */ }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Anuncio
        AdBanner(context = context)
    }
}

@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFC0CB))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "M-126",
            fontSize = 20.sp,
            color = Color.White
        )
        Spacer(modifier = Modifier.weight(1f))
        // Imagen de logo de la aplicación
        Image(
            painter = painterResource(id = R.drawable.logo_fullbus),
            contentDescription = "Logo FullBus",
            modifier = Modifier.size(40.dp)
        )
    }
}