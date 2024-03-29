package com.example.projecta2.util

import com.example.projecta2.api.GymService
import com.example.projecta2.api.UserService
import com.example.projecta2.model.User
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "http://10.0.2.2:8111/"
    private lateinit var authUser: User // 사용자 정보 저장용 변수

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //센터 조회
    val gymService: GymService by lazy {
        retrofit.create(GymService::class.java)
    }

    //유저 관련 Service
    val userService : UserService by lazy{
        retrofit.create(UserService::class.java)
    }


}
