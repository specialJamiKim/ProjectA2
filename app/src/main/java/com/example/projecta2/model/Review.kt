package com.example.projecta2.model

import com.example.projecta2.Entity.UserInfo

data class Review(
    val id: Long = 0,
    var user: UserInfo,
    var fitnessCenter: FitnessCenter,
    var rating: Int?,
    var reviewText: String
)
