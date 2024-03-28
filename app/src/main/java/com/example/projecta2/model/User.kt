package com.example.projecta2.model

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class User(
    val id: Long,
    var name: String,
    val email: String,
    var password: String,
    var phoneNumber: String,
    val gender: String,
    var address: String,
    val joinDate: String,
    val role: List<String>
)
