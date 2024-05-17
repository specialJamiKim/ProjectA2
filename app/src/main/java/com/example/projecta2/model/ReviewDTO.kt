package com.example.projecta2.model

//review 삭제를 위한 reviewIdm, userId 담은 객체 ReviewDTO
data class ReviewDTO(
    var reviewId: Long = 0,
    var userId : Long = 0
)