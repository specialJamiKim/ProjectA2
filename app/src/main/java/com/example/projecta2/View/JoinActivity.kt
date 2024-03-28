package com.example.projecta2.View

import android.app.DatePickerDialog

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager

import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projecta2.R
import com.example.projecta2.api.Join
import com.example.projecta2.api.SignIn
import com.example.projecta2.model.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Calendar

class JoinActivity : AppCompatActivity() {

    private lateinit var btnJoinPro : Button
    private lateinit var etName : EditText
    private lateinit var etEmail : EditText
    private lateinit var etPassword : EditText
    private lateinit var etPhoneNumber : EditText
    private lateinit var etBirthDate: EditText
    private lateinit var joinDate : String
    private lateinit var role : ArrayList<String>
    private lateinit var selectedGender : String

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
        role = ArrayList() // role 변수 초기화

        val radioGroup = findViewById<RadioGroup>(R.id.rgGender)
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = findViewById<RadioButton>(checkedId)
            selectedGender = radioButton.text.toString()
            Log.d("SelectedGender", "Selected gender: $selectedGender")
        }

        btnJoinPro.setOnClickListener {
            val user = createUserFromInput() // 사용자 입력에서 사용자 객체 생성
            fetchTestMessage(user)
        }
    }

    // 사용자 입력에서 사용자 객체 생성
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
            birthDate = etBirthDate.text.toString(),
            role = listOf("ROLE_USER")
        )
    }

    private fun fetchTestMessage(user: User) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8111/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val joinService = retrofit.create(Join::class.java)

        joinService.join(user).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        val message = it.string()
                        Log.d(">>>>", "Response message: $message")

                        // 회원가입 성공 메시지를 토스트로 표시
                        showToast("000님 회원가입 성공했습니다")
                        // 로그인 화면으로 이동
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

    // 토스트 메시지를 표시하는 함수
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
