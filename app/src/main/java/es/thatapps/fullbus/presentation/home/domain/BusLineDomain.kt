package es.thatapps.fullbus.presentation.home.domain

import androidx.compose.ui.graphics.Color

data class BusLineDomain(
    val line: String,
    val origin: String,
    val destination: String,
    val color: BusLineColorsDomain,
)

data class BusLineColorsDomain(
    val colorBase: Color,
    val colorAccent: Color,
)

object BusLineMockData {
    // Obtenber lista mokeada
    fun getMockBusLines(): List<BusLineDomain> {
        return listOf(
            BusLineDomain(
                line = "M-126",
                origin = "El Viso del Alcor",
                destination = "Sevilla",
                color = BusLineColorsDomain(
                    colorBase = Color(0xFFffa2c4),
                    colorAccent = Color(0xFFffb9d3)
                )
            ),
            BusLineDomain(
                line = "M-122",
                origin = "Alcalá de Guadaira",
                destination = "Sevilla",
                color = BusLineColorsDomain(
                    colorBase = Color(0xFFf9ff00),
                    colorAccent = Color(0xFFd0d700)
                )
            ),
            BusLineDomain(
                line = "M-124",
                origin = "Carmona",
                destination = "Sevilla",
                color = BusLineColorsDomain(
                    colorBase = Color(0xFFaf7c53),
                    colorAccent = Color(0xFFB17D54)
                )
            ),

        )
    }
}