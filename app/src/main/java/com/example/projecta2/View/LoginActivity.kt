package com.example.projecta2.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.projecta2.R
import com.example.projecta2.model.User
import com.example.projecta2.util.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var login: Button
    private lateinit var btnJoinPage: Button
    private lateinit var userEmailTextView: EditText
    private lateinit var userPwTextView: EditText
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnJoinPage = findViewById(R.id.btnJoinPage)
        userEmailTextView = findViewById(R.id.userEmailTextView)
        userPwTextView = findViewById(R.id.userPwTextView)
        login = findViewById(R.id.login)

        login.setOnClickListener {
            email = userEmailTextView.text.toString()
            password = userPwTextView.text.toString()
            signIn(email, password)
        }

        btnJoinPage.setOnClickListener {
            val intent = Intent(applicationContext, JoinActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signIn(email: String, password: String) {
        val userService = RetrofitInstance.userService

        userService.signIn(email, password).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        // 받은 User 객체의 이메일과 이름을 로그에 출력
                        Log.d(">>>>", "Email : ${user.email}, name : ${user.name} ${user.phoneNumber}, ${user.address}, 생일 > ${user.birthDate}")

                        // 로그인에 성공했으므로 HomeActivity로 이동
                        val intent = Intent(applicationContext, HomeActivity::class.java)
                        startActivity(intent)
                        finish() // 현재 액티비티를 종료하여 뒤로가기 버튼을 눌렀을 때 다시 돌아오지 않도록 함
                    } else {
                        // 사용자 객체가 null인 경우 처리할 내용 추가
                        Log.e("Response Error", "Received null user object")
                    }
                } else {
                    // 응답이 실패한 경우 처리할 내용 추가
                    Log.e("Response Error", "Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                // 요청이 실패한 경우 처리할 내용 추가
                Log.e("Request Failed", "Error: ${t.message}", t)
            }
        })
    }
}
