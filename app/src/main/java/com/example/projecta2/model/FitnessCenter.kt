package com.example.projecta2.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import java.time.LocalTime

@Serializable
data class FitnessCenter(
    val id: Long = 0,
    val name : String
)
