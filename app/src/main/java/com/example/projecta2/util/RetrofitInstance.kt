package com.example.projecta2.util

import com.example.projecta2.api.GymService
import com.example.projecta2.api.ReservationService

import com.example.projecta2.api.UserService
import com.example.projecta2.model.User
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    private const val BASE_URL = "http://10.100.103.49:8111/"

    private const val CONNECT_TIMEOUT_SECONDS = 30L // 연결 Timeout 시간 (초)
    private const val READ_TIMEOUT_SECONDS = 30L // 읽기 Timeout 시간 (초)
    private lateinit var authUser: User // 사용자 정보 저장용 변수

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .build()

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

    val reservationService : ReservationService by lazy{
        retrofit.create(ReservationService::class.java)
    }


}