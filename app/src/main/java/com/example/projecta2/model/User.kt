package com.example.projecta2.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.io.Serial
import java.time.LocalDate
import java.util.Date

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
/*    @Contextual
    var birthDate : LocalDate, // Date 형식으로 변경*/
    var joinDate: String, // 여전히 String 형식으로 유지
    var role: List<String>
) {
    constructor(id: Long) : this(id, "", "", "", "", "", "", "", "", emptyList())
}