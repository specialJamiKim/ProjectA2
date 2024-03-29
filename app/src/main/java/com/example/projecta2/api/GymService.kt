package com.example.projecta2.api

import com.example.projecta2.model.FitnessCenter
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface GymService {
    @FormUrlEncoded
    @POST("/m_centerManage/gymList")
    fun getGymList(@Field("id") id: Long): Call<FitnessCenter>
}
