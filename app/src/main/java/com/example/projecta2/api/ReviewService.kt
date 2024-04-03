package com.example.projecta2.api

import com.example.projecta2.model.Review
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ReviewService {

    @POST("/m_review/add")
    suspend fun addReview(@Body review: Review): Call<ResponseBody>

    // 모든 리뷰 조회
    @GET("/m_review/all")
    fun getAllReviews(): Call<List<Review>>
}