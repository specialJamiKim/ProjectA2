package com.example.projecta2.model

import java.util.Date

data class User(
    val id: Long = 0,
    var name: String,
    val email: String,
    var password: String,
    var phoneNumber: String,
    val gender: String,
    var address: String,
    val joinDate: Date,
    val role: List<String>
)
