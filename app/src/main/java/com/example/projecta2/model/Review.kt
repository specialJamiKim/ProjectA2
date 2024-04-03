package com.example.projecta2.model

data class Review(
    val id: Long = 0,
    var userId: Long?,
    var centerId: Long,
    var rating: Int?,
    var reviewText: String
)
