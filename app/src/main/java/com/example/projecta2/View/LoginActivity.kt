package com.example.projecta2.View

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.projecta2.R
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class LoginActivity : AppCompatActivity() {

    private lateinit var login: Button
    private lateinit var btnTest: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login = findViewById(R.id.login)
        login.setOnClickListener {
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
        }
        btnTest = findViewById<Button>(R.id.btnTest)
        btnTest.setOnClickListener {
            fetchTestMessage()
        }
    }

    private fun fetchTestMessage() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8111/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(TestService::class.java)

        service.testSend().enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    Log.d("Received Data >> ", "Name: ${user?.name}, Email: ${user?.email}")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("Request Failed", "Error: ${t.message}", t)
            }
        })
    }

    data class User(val name: String?, val email: String?)

    interface TestService {
        @GET("/m_user/tototo")
        fun testSend(): Call<User>
    }
}
