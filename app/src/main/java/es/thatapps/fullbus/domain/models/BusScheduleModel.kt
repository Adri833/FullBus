package es.thatapps.fullbus.domain.models

data class BusScheduleModel(
    val line: String,
    val schedules: Map<String, Map<String, Map<String, List<String>>>> // [Tipo de dia -> Direccion -> Horas]
)
