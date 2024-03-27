package com.example.projecta2.model

import java.time.LocalTime

data class FitnessCenter(
    val id: Long = 0 ,
    var name: String,
    var address: String,
    var phoneNumber: String,
    var dailyPassPrice: Long,
    var openTime: LocalTime,
    var closingTime: LocalTime,
    var imagePath: String?=null,
)
