package es.thatapps.fullbus.presentation.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import es.thatapps.fullbus.presentation.components.BusInfoBox
import es.thatapps.fullbus.presentation.components.Header
import es.thatapps.fullbus.presentation.home.domain.BusLineMockData

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigationToRegister: () -> Unit,
    navigationToSettings: () -> Unit,
) {
    // TODO: quitar comentarios del contexto y de anuncio
//    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header sin padding para que ocupe toda la pantalla horizontalmente
        Header(navigationToRegister, navigationToSettings)

        Spacer(modifier = Modifier.height(15.dp))

        // Contenido principal de la página con padding
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                // Se ancla entre el Header y el AdBanner
                modifier = Modifier.weight(1f),
                content = {
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
            }
            )

            // Espacio en blanco hasta la parte inferior de la pantalla
            Spacer(modifier = Modifier.height(10.dp))

            // Banner de anuncio en la parte inferior
//            AdBanner(context)

            Spacer(modifier = Modifier.height(46.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    HomeScreen(
        navigationToRegister = {},
        navigationToSettings = {},
    )
}