package com.example.projecta2.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.projecta2.R
import com.example.projecta2.api.SignIn
import com.example.projecta2.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var login: Button
    private lateinit var userEmailTextView: TextView
    private lateinit var userPwTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userEmailTextView = findViewById(R.id.userEmailTextView)
        userPwTextView = findViewById(R.id.userPwTextView)

        login = findViewById(R.id.login)
        login.setOnClickListener {
            val email = userEmailTextView.text.toString()
            val password = userPwTextView.text.toString()
            fetchTestMessage(email, password)
        }
    }

    private fun fetchTestMessage(email: String, password: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8111/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(SignIn::class.java)

        service.testSend(email, password).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    user?.let {
                        // 받은 User 객체의 이메일과 이름을 TextView에 설정
                        Log.d(">>>>", "Email : ${it.email}, name : ${it.name} ${it.address}")

                        // 로그인에 성공했으므로 HomeActivity로 이동
                        val intent = Intent(applicationContext, HomeActivity::class.java)
                        startActivity(intent)
                        finish() // 현재 액티비티를 종료하여 뒤로가기 버튼을 눌렀을 때 다시 돌아오지 않도록 함
                    }
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("Request Failed", "Error: ${t.message}", t)
            }
        })
    }

}
