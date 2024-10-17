package es.thatapps.fullbus.presentation.main

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import es.thatapps.fullbus.R
import es.thatapps.fullbus.presentation.components.AdBanner
import es.thatapps.fullbus.presentation.components.BusInfoBox
import es.thatapps.fullbus.presentation.components.Header


@Composable
fun MainScreen() {
    val context = LocalContext.current // Obtiene el contexto actual

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header sin padding para que ocupe toda la pantalla horizontalmente
        Header()

        Spacer(modifier = Modifier.height(15.dp))

        // Contenido principal de la página con padding
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Viso -> Sevilla
            BusInfoBox(
                lineNumber = "M-126",
                backgroundColor1 = Color(0xFFffb9d3), // Color rosa claro
                backgroundColor2 = Color(0xFFffa2c4), // Color rosa oscuro
                origin = "Viso",
                destination = "Sevilla",
                busIcon = R.drawable.ic_bus, // Logo del bus
                onClick = { Toast.makeText(context, "Tontopolla", Toast.LENGTH_SHORT).show()}
            )

            // Sevilla -> Viso
            BusInfoBox(
                lineNumber = "M-126",
                backgroundColor1 = Color(0xFFffa2c4), // Colores a la inversa
                backgroundColor2 = Color(0xFFffb9d3),
                origin = "Sevilla",
                destination = "Viso",
                busIcon = R.drawable.ic_bus,
                onClick = { Toast.makeText(context, "Has clicado", Toast.LENGTH_SHORT).show()}
            )

            // Espacio en blanco hasta la parte inferior de la pantalla
            Spacer(modifier = Modifier.weight(1f))

            // Solo muestra el banner si el contexto no es nulo
            AdBanner(context)

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    MainScreen()
}