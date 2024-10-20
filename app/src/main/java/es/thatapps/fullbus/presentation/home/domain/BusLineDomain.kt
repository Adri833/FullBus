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
                    colorBase = Color(0xFFffb9d3),
                    colorAccent = Color(0xFFffa2c4)
                )
            ),
            BusLineDomain(
                line = "M-123B",
                origin = "East Station",
                destination = "West Gate",
                color = BusLineColorsDomain(
                    colorBase = Color(0xFFffa2c4),
                    colorAccent = Color(0xFFffb9d3)
                )
            ),
            BusLineDomain(
                line = "C3",
                origin = "Downtown",
                destination = "City Airport",
                color = BusLineColorsDomain(
                    colorBase = Color(0xFFffa2c4),
                    colorAccent = Color(0xFFffb9d3)
                )
            ),
            BusLineDomain(
                line = "D4",
                origin = "South Avenue",
                destination = "North Hill",
                color = BusLineColorsDomain(
                    colorBase = Color(0xFFffb9d3),
                    colorAccent = Color(0xFFffa2c4)
                )
            ),
        )
    }
}