package es.thatapps.fullbus.presentation.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import es.thatapps.fullbus.presentation.components.AdBanner
import es.thatapps.fullbus.presentation.components.BusInfoBox
import es.thatapps.fullbus.presentation.components.Header
import es.thatapps.fullbus.presentation.home.domain.BusLineMockData

@Composable
fun MainScreen(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

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
            LazyColumn(content = {
                itemsIndexed(BusLineMockData.getMockBusLines()) { _, item ->
                    item.apply {
                        BusInfoBox(
                            lineNumber = line,
                            backgroundColor1 = color.colorBase,
                            backgroundColor2 = color.colorAccent,
                            origin = origin,
                            destination = destination,
                        ) {
//                            TODO onCLick
                        }
                    }
                }
            }, modifier = Modifier
                .fillMaxSize()
                .height(360.dp)
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