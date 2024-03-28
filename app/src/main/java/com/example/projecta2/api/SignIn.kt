package com.example.projecta2.api

import com.example.projecta2.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface SignIn {
    @FormUrlEncoded
    @POST("/loginPro")
    fun signIn(@Field("email") email: String, @Field("password") password: String): Call<User>
}
