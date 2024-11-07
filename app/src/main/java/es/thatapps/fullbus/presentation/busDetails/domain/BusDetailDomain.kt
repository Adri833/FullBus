package es.thatapps.fullbus.presentation.busDetails.domain

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

data class BusDetailDomain(
    val line: String = "",
    var isFull: Boolean = false,
    var departureTime: String = "",
    var arriveTime: String = ""
)