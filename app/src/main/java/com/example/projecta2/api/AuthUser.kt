package com.example.projecta2.api

import com.example.projecta2.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthUser {
    @POST("/authUser")
    fun getAuthUser(): Call<User>
}
