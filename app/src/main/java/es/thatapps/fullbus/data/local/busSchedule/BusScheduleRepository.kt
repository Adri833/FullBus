package es.thatapps.fullbus.data.local.busSchedule

import es.thatapps.fullbus.domain.models.BusScheduleModel

object BusScheduleRepository {
    val busSchedules = listOf(
        BusScheduleModel(
            line = "M-126",
            schedules = mapOf(
                "Winter" to mapOf(
                    "Normal" to mapOf(
                        "Ida" to listOf(
                            "05:40", "06:10", "06:35", "06:35", "07:00", "07:30", "08:00", "08:30", "09:00", "09:30", "10:15", "11:00", "11:30", "12:00", "12:30",
                            "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:20", "17:00", "17:45", "18:15", "19:00", "19:40", "20:20", "21:40"
                        ),
                        "Vuelta" to listOf(
                            "06:30", "07:05", "07:35", "08:00", "08:30", "09:15", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00",
                            "13:30", "14:00", "14:30", "15:10", "15:40", "16:40", "17:40", "18:30", "19:20", "20:40", "21:20", "22:30", "23:30"
                        )
                    ),
                    "Saturday" to mapOf(
                        "Ida" to listOf("06:45", "07:45", "09:45", "12:00", "13:30", "14:00", "16:00", "18:00", "20:00", "22:15"),
                        "Vuelta" to listOf("07:45", "08:45", "11:00", "12:00", "13:00", "15:00", "17:00", "19:00", "21:15", "00:15")
                    ),
                    "Holiday" to mapOf(
                        "Ida" to listOf("07:30", "09:00", "11:00", "14:00", "16:15", "18:30", "21:00"),
                        "Vuelta" to listOf("07:30", "09:00", "12:15", "15:15", "17:15", "20:00", "23:00")
                    )
                ),
                "Summer" to mapOf(
                    "Normal" to mapOf(
                        "Ida" to listOf(
                            "05:35", "06:30", "07:00", "07:25", "08:25", "09:25", "10:25", "11:40", "12:40",
                            "13:45", "14:45", "15:45", "16:45", "17:45", "19:15", "20:00", "21:30"
                        ),
                        "Vuelta" to listOf(
                            "06:30", "07:30", "08:25", "09:25", "10:40", "11:40", "12:40", "13:40",
                            "14:45", "15:45", "16:45", "17:45", "19:00", "20:30", "22:00", "23:30"
                        )
                    ),
                    "Saturday" to mapOf(
                        "Ida" to listOf("06:45", "07:45", "09:45", "12:00", "13:30", "14:00", "16:00", "18:00", "20:00", "22:15"),
                        "Vuelta" to listOf("07:45", "08:45", "11:00", "12:00", "13:00", "15:00", "17:00", "19:00", "21:15", "00:15")
                    ),
                    "Holiday" to mapOf(
                        "Ida" to listOf("08:00", "10:00", "12:30", "14:30", "16:30", "18:30", "21:00"),
                        "Vuelta" to listOf("09:00", "11:30", "13:30", "15:30", "17:30", "20:00", "23:00")
                    )
                )
            )
        ),

        BusScheduleModel(
            line = "M-122",
            schedules = mapOf(
                "Winter" to mapOf(
                    "Normal" to mapOf(
                        "Ida" to listOf(
                            "06:45", "06:55", "07:05", "07:15", "07:25", "07:40", "08:00", "08:10", "08:25", "08:35", "08:40", "08:55", "09:10", "09:25", "09:40", "09:50", "09:55", "10:10", "10:35", "11:00", "11:05", "11:20", "11:45", "12:10", "12:35", "12:55", "13:20", "13:30", "13:50",
                            "14:05", "14:20", "14:35", "14:50", "15:20", "15:35", "16:00", "16:30", "16:55", "17:15", "17:40", "18:05", "18:25", "18:50", "19:15", "19:45", "20:10", "20:35", "21:05", "21:30"
                        ),
                        "Vuelta" to listOf(
                            "07:25", "07:35", "07:50", "08:00", "08:05", "08:20", "08:35", "08:50", "09:05", "09:15", "09:20", "09:35", "10:00", "10:25", "10:30", "10:45", "11:10", "11:35", "12:00", "12:20", "12:45", "13:10", "13:30", "13:45", "14:00",
                            "14:10", "14:30", "14:45", "15:00", "15:20", "15:35", "15:55", "16:20", "16:40", "17:05", "17:30", "17:50", "18:15", "18:40", "19:05", "19:30", "20:00", "20:25", "20:50", "21:45"
                        )
                    ),
                    "Saturday" to mapOf(
                        "Ida" to listOf("23:35", "00:50", "01:55", "03:00", "04:25"),
                        "Vuelta" to listOf("00:20", "01:25", "02:30", "03:55", "05:00")
                    )
                ),
                "Summer" to mapOf(
                    "Normal" to mapOf(
                        "Ida" to listOf(
                            "06:40", "07:05", "07:30", "07:50", "08:15", "08:40", "09:05", "09:30", "09:50", "10:15", "10:40", "11:25", "12:10", "12:40",
                            "13:20", "13:55", "14:35", "14:55", "15:15", "15:45", "16:25", "17:05", "17:50", "18:30", "19:00", "19:40", "20:10"
                        ),
                        "Vuelta" to listOf(
                            "07:20", "07:45", "08:10", "08:30", "08:55", "09:20", "09:45", "10:10", "10:55", "11:40", "12:05", "12:50", "13:25", "14:05",
                            "14:25", "14:45", "15:15", "15:35", "15:55", "16:35", "17:20", "18:00", "18:30", "19:10", "19:40", "20:20", "20:50"
                        ),
                    )
                )
            )
        ),

        BusScheduleModel(
            line = "M-124",
            schedules = mapOf(
                "Winter" to mapOf(
                    "Normal" to mapOf(
                        "Ida" to listOf(
                            "06:00", "06:45", "07:00", "07:15", "07:45", "08:05", "08:35", "08:45", "09:25", "10:00", "11:00", "12:00", "13:00", "14:00",
                            "14:30", "15:10", "15:40", "16:30", "17:30", "18:00", "18:30", "19:45", "21:00"
                        ),
                        "Vuelta" to listOf(
                            "06:45", "07:45", "08:25", "09:00", "10:00", "11:00", "12:00", "13:00", "13:35", "14:00", "14:30", "15:00",
                            "15:30", "16:30", "17:30", "18:30", "19:15", "20:00", "21:00", "22:00", "23:00"
                        )
                    ),
                    "Saturday" to mapOf(
                        "Ida" to listOf("06:30", "08:00", "09:00", "10:00", "11:15", "13:15", "13:30", "16:30", "19:00", "21:00"),
                        "Vuelta" to listOf("07:45", "09:00", "10:15", "12:00", "12:15", "14:30", "15:30", "18:00", "20:00", "23:00")
                    ),
                    "Holiday" to mapOf(
                        "Ida" to listOf("06:30", "08:45", "11:30", "13:45", "15:30", "18:00", "20:30"),
                        "Vuelta" to listOf("07:30", "10:30", "12:15", "14:30", "17:00", "19:00", "21:30")
                    )
                ),
                "Summer" to mapOf(
                    "Normal" to mapOf(
                        "Ida" to listOf(
                            "06:10", "06:45", "07:15", "07:40", "08:10", "08:45", "09:10", "10:30", "12:00",
                            "13:30", "14:30", "15:40", "16:10", "16:40", "17:30", "18:30", "19:30", "21:00"
                        ),
                        "Vuelta" to listOf(
                            "07:00", "08:05", "09:20", "10:30", "12:00", "13:20", "14:00", "14:30",
                            "15:35", "16:30", "17:30", "18:30", "19:15", "20:00", "21:30", "23:00"
                        )
                    ),
                    "Saturday" to mapOf(
                        "Ida" to listOf(
                            "06:30", "08:00", "09:00", "10:00", "11:15", "13:15", "13:30", "16:30", "19:00", "21:00"
                        ),
                        "Vuelta" to listOf(
                            "07:45", "09:00", "10:15", "12:00", "12:15", "14:30", "15:30", "18:00", "20:00", "23:00"
                        )
                    ),
                    "Holiday" to mapOf(
                        "Ida" to listOf(
                            "08:00", "10:00", "12:30", "14:30", "16:30", "19:00", "21:00"
                        ),
                        "Vuelta" to listOf(
                            "09:00", "11:30", "13:30", "15:30", "18:00", "20:00", "23:00"
                        )
                    )
                )
            )
        )
    )
}