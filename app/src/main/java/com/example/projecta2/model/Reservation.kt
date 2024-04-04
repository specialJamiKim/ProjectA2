package com.example.projecta2.model

import com.google.gson.annotations.SerializedName

data class Reservation(
    val id: Long = 0,
    @SerializedName("center")
    val center: FitnessCenter,
    @SerializedName("user")
    val user: User,
    @SerializedName("reservationTime")
    val reservationTime: String?
)
