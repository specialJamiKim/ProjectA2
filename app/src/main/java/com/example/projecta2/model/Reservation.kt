package com.example.projecta2.model

data class Reservation(

    val id: Long = 0,
    val center: FitnessCenter,
    val user: User,
    val reservationTime: String?
)

