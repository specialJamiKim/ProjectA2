package com.example.projecta2.api

import com.example.projecta2.model.User
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserSelector {
    @FormUrlEncoded
    @POST("/m_user/userSelect")
    fun userSelect(@Field("email") email: String): Call<User>
}
