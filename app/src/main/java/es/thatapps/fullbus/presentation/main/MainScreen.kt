package es.thatapps.fullbus.presentation.main

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import es.thatapps.fullbus.R
import es.thatapps.fullbus.presentation.components.AdBanner
import es.thatapps.fullbus.presentation.components.BusInfoBox
import es.thatapps.fullbus.presentation.components.Header

@Composable
fun MainScreen(context: Context) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header sin padding para que ocupe toda la pantalla horizontalmente
        Header()

        Spacer(modifier = Modifier.height(15.dp))

        // Contenido principal de la página con padding
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BusInfoBox(
                lineNumber = "M-126",
                backgroundColor = Color(0xFFFFC0CB), // Color rosa
                origin = "Viso",
                destination = "Sevilla",
                busIcon = R.drawable.ic_bus // Logo del bus
            )

            Spacer(modifier = Modifier.height(20.dp))
            BusInfoBox(
                lineNumber = "M-126",
                backgroundColor = Color(0xFFFFC0CB), // Color rosa
                origin = "Sevilla",
                destination = "Viso",
                busIcon = R.drawable.ic_bus,
                reversed = true
            )

            // Espacio en blanco hasta la parte inferior de la pantalla
            Spacer(modifier = Modifier.weight(1f))

            // Banner de anuncio
            AdBanner(context)

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
