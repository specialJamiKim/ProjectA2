package com.example.projecta2.api

import com.example.projecta2.model.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Join {
    @POST("/m_user/join")
    fun join(@Body user: User): Call<ResponseBody>

}
