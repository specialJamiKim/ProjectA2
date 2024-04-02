package com.example.projecta2.model

import java.util.Date

data class Reservation(
    val id: Long,
    var center: FitnessCenter,
    var user: User,
    var reservationTime: String,

)
