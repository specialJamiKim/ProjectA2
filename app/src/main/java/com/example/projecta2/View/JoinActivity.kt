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
import androidx.appcompat.app.AppCompatActivity
import com.example.projecta2.R
import com.example.projecta2.model.User
import com.example.projecta2.util.DialogHelper
import com.example.projecta2.util.RetrofitInstance
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JoinActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etPhoneNumber: EditText
    private lateinit var etBirthDate: EditText
    private lateinit var joinDate: String
    private lateinit var selectedGender: String
    private lateinit var selectedGenderEng: String

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

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
            selectedGenderEng = if (selectedGender.equals("남자")) "mail" else "female"
        }

        findViewById<Button>(R.id.btnJoinPro).setOnClickListener {
            val user = createUserFromInput()
            checkEmailAndSignUp(user)
        }
    }

    private fun createUserFromInput(): User {
        return User(
            id = 0,
            name = etName.text.toString(),
            email = etEmail.text.toString(),
            password = etPassword.text.toString(),
            phoneNumber = etPhoneNumber.text.toString(),
            gender = selectedGenderEng,
            address = "부산광역시",
            joinDate = joinDate,
            birthDate = "1995-04-28",
            role = listOf("ROLE_USER")
        )
    }

    private fun checkEmailAndSignUp(user: User) {
        val userService = RetrofitInstance.userService
        userService.inquiryEmail(user.email).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    userJoin(user)
                } else {
                    val errorBody = response.errorBody()?.string()
                    if (response.code() == 409) {
                        DialogHelper.showMessageDialog(
                            this@JoinActivity,
                            "사용중인 email",
                            "사용중인 이메일입니다.\n변경 후, 다시 시도해 주세요."
                        )
                        etEmail.requestFocus()
                    } else {
                        DialogHelper.showMessageDialog(
                            this@JoinActivity,
                            "중복 확인 실패",
                            "이메일 중복 확인 실패: $errorBody"
                        )
                    }
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("Check Email", "Error: ${t.message}", t)
                DialogHelper.showMessageDialog(
                    this@JoinActivity,
                    "통신 실패",
                    "서버와 통신이 실패했습니다.\n연결을 확인해주세요."
                )
            }
        })
    }

    private fun userJoin(user: User) {
        val userService = RetrofitInstance.userService

        userService.join(user).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    DialogHelper.showMessageDialog(
                        this@JoinActivity,
                        "회원가입 성공",
                        "${etName.text.toString()}님\n회원가입에 성공했습니다."
                    ) {
                        val intent = Intent(applicationContext, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("Request Failed", "Error: ${t.message}", t)
                DialogHelper.showMessageDialog(
                    this@JoinActivity,
                    "통신 실패",
                    "서버와 통신이 실패했습니다.\n연결을 확인해주세요."
                )
            }
        })
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
