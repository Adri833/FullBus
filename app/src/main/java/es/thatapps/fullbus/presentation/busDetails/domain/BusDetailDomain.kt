package es.thatapps.fullbus.presentation.busDetails.domain

data class BusDetailDomain(
    val line: String = "",
    var isFull: Boolean = false,
    var departureTime: String = "",
    var arriveTime: String = "",
    var id: String = "",
    var day: String = "",
    )