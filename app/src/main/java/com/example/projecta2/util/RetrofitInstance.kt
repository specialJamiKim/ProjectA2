package com.example.projecta2.util

import com.example.projecta2.api.GymService
import com.example.projecta2.api.Join
import com.example.projecta2.api.SignIn
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "http://10.100.103.27:8111/"

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

    //회원가입
    val joinService : Join by lazy{
        retrofit.create(Join::class.java)
    }

    //로그인
    val signService : SignIn by lazy{
        retrofit.create(SignIn::class.java)
    }

}
