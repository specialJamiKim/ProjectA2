package com.example.projecta2.util

import com.example.projecta2.api.GymService
import com.example.projecta2.api.UserService
import com.example.projecta2.api.ReviewService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "http://10.0.2.2:8111/" // 실제 서버 주소로 변경하세요.

    // HTTP 로깅 인터셉터를 추가합니다. 네트워크 요청 및 응답을 로그로 출력하도록 설정합니다.
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // 요청 및 응답 본문 로깅
    }

    // Retrofit에 로깅 인터셉터를 포함하는 OkHttpClient 인스턴스를 생성합니다.
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // Retrofit 인스턴스를 초기화합니다. OkHttpClient와 GsonConverterFactory를 사용합니다.
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL) // 베이스 URL 설정
            .client(okHttpClient) // 로깅을 위한 클라이언트 추가
            .addConverterFactory(GsonConverterFactory.create()) // JSON 파서로 Gson 사용
            .build()
    }

    // 센터 조회를 위한 GymService 인터페이스의 구현체를 생성합니다.
    val gymService: GymService by lazy {
        retrofit.create(GymService::class.java)
    }

    // 사용자 관련 서비스를 위한 UserService 인터페이스의 구현체를 생성합니다.
    val userService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }

    // 리뷰 관련 서비스를 위한 ReviewService 인터페이스의 구현체를 생성합니다.
    val reviewService: ReviewService by lazy {
        retrofit.create(ReviewService::class.java)
    }
}
