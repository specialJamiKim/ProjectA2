package com.example.projecta2.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import java.io.Serial
import java.time.LocalDate

@Serializable
data class User(
    var id: Long,
    var name: String,
    @SerializedName(value = "email")
    var email: String,
    @SerializedName(value = "password")
    var password: String,
    var phoneNumber: String,
    var gender: String,
    var address: String,
    var birthDate : String,
    var joinDate: String,
    var role: List<String>
)