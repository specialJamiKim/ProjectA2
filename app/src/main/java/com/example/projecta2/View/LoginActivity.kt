package com.example.projecta2.View

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
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
    private lateinit var btnJoinPage: Button
    private lateinit var userEmailTextView: EditText
    private lateinit var userPwTextView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 타이틀바 숨기기
        supportActionBar?.hide()

        userEmailTextView = findViewById(R.id.userEmailTextView)
        userPwTextView = findViewById(R.id.userPwTextView)
        login = findViewById(R.id.login)
        btnJoinPage = findViewById(R.id.btnJoinPage)

        login.setOnClickListener {
            val email = userEmailTextView.text.toString().trim()
            val password = userPwTextView.text.toString().trim()
            if (email.isEmpty() || password.isEmpty()) {
                showAlert("입력란에 공백이 있습니다.")
            } else {
                signIn(email, password)
            }
        }

        btnJoinPage.setOnClickListener {
            val intent = Intent(applicationContext, JoinActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signIn(email: String, password: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8111/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(SignIn::class.java)

        service.signIn(email, password).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()

                    if (user != null) {
                        Log.d("LoginActivity", "Email : ${user.email}, name : ${user.name}")
                        val intent = Intent(applicationContext, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Log.e("LoginActivity", "Received null user object")
                    }
                } else {
                    Log.e("LoginActivity", "Response Error: Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("LoginActivity", "Request Failed: Error: ${t.message}", t)
            }
        })
    }

    private fun showAlert(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("확인", null)
            .show()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            if (view is EditText) {
                val outRect = android.graphics.Rect()
                view.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    view.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }


}
