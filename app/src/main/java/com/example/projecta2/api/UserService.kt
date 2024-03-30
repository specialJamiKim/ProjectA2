package com.example.projecta2.api

import com.example.projecta2.model.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {
    //인증된 객체 가져오기
    @POST("/m_user/getAuthUser")
    fun getAuthUser(): Call<User>

    //로그인 처리
    @FormUrlEncoded
    @POST("/loginPro")
    fun signIn(@Field("email") email: String, @Field("password") password: String): Call<User>

    //회원가입
    @POST("/m_user/join")
    fun join(@Body user: User): Call<ResponseBody>

    //회원가입 => 아이디 중복검사(email)
    @FormUrlEncoded
    @POST("/m_user/inquiryEmail")
    fun inquiryEmail(@Field("email")email : String) : Call<Void>
}