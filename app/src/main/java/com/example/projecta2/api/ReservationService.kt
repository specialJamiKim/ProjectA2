package com.example.projecta2.api

import com.example.projecta2.model.Reservation
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReservationService {

    @POST("/m_reservation/create")
    fun createReservation(@Body reservation: Reservation): Call<ResponseBody>

    @GET("/m_reservation/list/{reservationId}")
    fun getUserReservations(@Path("reservationId") reservationId: Long): Call<List<Reservation>>

}

