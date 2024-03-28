package com.example.projecta2.api

import com.example.projecta2.model.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SignIn {
    @GET("/loginPro")
    fun testSend(@Query("email") email: String, @Query("password") password: String): Call<User>
}
