package es.thatapps.fullbus.data.local.busSchedule

import es.thatapps.fullbus.domain.models.BusScheduleModel

object BusScheduleRepository {
    val busSchedules = listOf(
        BusScheduleModel(
            line = "M-126",
            normalSchedule = listOf(
                "5:40", "6:10", "6:35", "6:35", "7:00", "7:30", "8:00", "8:30", "9:00", "9:30", "10:15", "11:00", "11:30", "12:00", "12:30",
                "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:20", "17:00", "17:45", "18:15", "19:00", "19:40", "20:20", "21:40"
            ),
            saturdaySchedule = listOf(
                "06:45", "07:45", "09:45", "12:00", "13:30", "14:00", "16:00",
                "18:00", "20:00", "22:15"
            ),
            holidaySchedule = listOf(
                "7:30", "9:00", "11:00", "14:00", "16:15", "18:30", "21:00"
            )
        ),

        BusScheduleModel(
            line = "M-124",
            normalSchedule = listOf(
                "6:00", "6:45", "7:00", "7:15", "7:45", "8:05", "8:35", "8:45", "9:25", "10:00", "11:00",
                "12:00", "13:00", "14:00", "14:30", "15:10", "15:40", "16:30", "17:30", "18:00", "18:30", "19:45", "21:00"
            ),
            saturdaySchedule = listOf(
                "6:30", "8:00", "9:00", "10:00", "11:15", "13:15", "13:30", "16:30", "19:00", "21:00"
            ),
            holidaySchedule = listOf(
                "6:30", "8:45", "11:30", "13:45", "15:30", "18:00", "20:30"
            )
        )
    )
}