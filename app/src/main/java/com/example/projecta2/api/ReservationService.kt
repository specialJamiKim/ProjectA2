package com.example.projecta2.api

import com.example.projecta2.model.Reservation
import com.example.projecta2.model.Result
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ReservationService {

    //예약저장
    @POST("/m_reservation/create")
    fun createReservation(@Body reservation: Reservation): Call<ResponseBody>

    //예약 리스트 가져오기
    /*    @POST("/m_reservation/list")
        @FormUrlEncoded
        fun getUserReservations(@Field("userId") userId: Long?): Call<List<Reservation>>*/
//예약 리스트 가져오기
    /*
        @POST("/m_reservation/list")
        @FormUrlEncoded
        fun getUserReservations(@Field("userId") userId: Long?): Call<ResponseBody>
    */


    // 예약 리스트 가져오기
    @POST("/m_reservation/list")
    @FormUrlEncoded
    fun getUserReservations(@Field("userId") userId: Long?): Call<Result<Reservation>>




}

