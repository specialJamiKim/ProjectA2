package com.example.projecta2.model

data class Review(
    val id: Long = 0,
    var user: User,
    var fitnessCenter: FitnessCenter,
    var rating: Int,
    var reviewText:String
)
