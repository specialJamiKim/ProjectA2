package com.example.projecta2.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import java.time.LocalTime

@Serializable
data class FitnessCenter(
    val id: Long = 0,
    val name : String,
    val dailyPassPrice : Long,
    val distance : Long?,
    val address : String,
    val imagePath: String? // 이미지 경로 변수 추가
)