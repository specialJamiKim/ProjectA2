package com.example.projecta2.api

import com.example.projecta2.model.Review
import retrofit2.Call
import retrofit2.http.GET

interface ReviewService {
    @GET("m_review/all")
    fun getAllReviews(): Call<List<Review>>
}