package com.example.projecta2.util

import android.content.Context

import com.example.projecta2.api.UserService
import com.example.projecta2.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SessionManager(private val userService: UserService) {

    private var authUser: User? = null

    // 세션에서 인증된 사용자 객체 가져오기
    fun getAuthUser(callback: (User?) -> Unit) {
        if (authUser != null) {
            // 이미 인증된 사용자 정보가 로컬에 있는 경우
            callback(authUser)
        } else {
            // 서버에서 인증된 사용자 정보를 가져옴
            userService.getAuthUser().enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        authUser = response.body()
                        callback(authUser)
                    } else {
                        // 인증된 사용자 정보를 가져오지 못한 경우
                        callback(null)
                    }
                }
                override fun onFailure(call: Call<User>, t: Throwable) {
                    // 통신 실패 처리
                    callback(null)
                }
            })
        }
    }
}
