package com.example.projecta2.View

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projecta2.R
import com.example.projecta2.api.Join
import com.example.projecta2.model.User
import com.example.projecta2.util.RetrofitInstance
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JoinActivity : AppCompatActivity() {

    private lateinit var btnJoinPro: Button
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etPhoneNumber: EditText
    private lateinit var etBirthDate: EditText
    private lateinit var joinDate: String
    private lateinit var selectedGender: String

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        btnJoinPro = findViewById(R.id.btnJoinPro)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etPhoneNumber = findViewById(R.id.etPhoneNumber)
        etBirthDate = findViewById(R.id.etBirthDate)
        joinDate = "1995-04-28"

        val radioGroup = findViewById<RadioGroup>(R.id.rgGender)
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val radioButton = findViewById<RadioButton>(checkedId)
            selectedGender = radioButton.text.toString()
            Log.d("SelectedGender", "Selected gender: $selectedGender")
        }

        btnJoinPro.setOnClickListener {
            val user = createUserFromInput()
            userJoin(user)
        }
    }

    private fun createUserFromInput(): User {
        return User(
            id = 0,
            name = etName.text.toString(),
            email = etEmail.text.toString(),
            password = etPassword.text.toString(),
            phoneNumber = etPhoneNumber.text.toString(),
            gender = selectedGender,
            address = "부산광역시",
            joinDate = joinDate,
            birthDate = "1995-04-28",
            role = listOf("ROLE_USER")
        )
    }

    private fun userJoin(user: User) {
        val joinService = RetrofitInstance.joinService

        joinService.join(user).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        val message = it.string()
                        Log.d(">>>>", "Response message: $message")
                        showToast("회원가입 성공했습니다")
                        val intent = Intent(applicationContext, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("Request Failed", "Error: ${t.message}", t)
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = android.graphics.Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}
