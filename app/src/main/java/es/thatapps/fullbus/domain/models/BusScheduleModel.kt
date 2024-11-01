package es.thatapps.fullbus.domain.models

data class BusScheduleModel(
    val line: String,
    val normalSchedule: List<String>,
    val saturdaySchedule: List<String>,
    val holidaySchedule: List<String>
)