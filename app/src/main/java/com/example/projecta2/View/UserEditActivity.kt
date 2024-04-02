package com.example.projecta2.View

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.widget.EditText
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projecta2.Dao.UserDB
import com.example.projecta2.R
import com.example.projecta2.api.UserService
import com.example.projecta2.model.User
import com.example.projecta2.util.RetrofitInstance
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.ResponseBody

class UserEditActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var telEditText: EditText
    private lateinit var birthEditText: EditText
    private lateinit var submitButton: Button

    //  private lateinit var deleteButton: Button
    private lateinit var userEmail: String
    private lateinit var userService: UserService
    // Room Database instance
    lateinit var db: UserDB

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_edit)
        userService = RetrofitInstance.userService
        //db 싱글톤 가져옴
        db = DatabaseInitializer.initDatabase(this)
        // 액션 바가 있다면 숨깁니다.
        supportActionBar?.hide()


        // Retrieve email value when needed
        userEmail = SessionManager.getUserEmail(this).toString()


        // EditText 초기화
        nameEditText = findViewById(R.id.userEditName)
        addressEditText = findViewById(R.id.userEditAddress)
        telEditText = findViewById(R.id.userEditTel)
        birthEditText = findViewById(R.id.userEditBirth)

        // 사용자 정보 가져오기(수정예정)
        getUserInfo(userEmail)




        // 수정 버튼 클릭 리스너 설정
        submitButton = findViewById(R.id.editBth)
        submitButton.setOnClickListener {
            // 수정된 정보를 가져와서 처리하는 함수 호출
            userUpdate()
        }

        // 삭제 버튼 클릭 리스너 설정
        val deleteButton = findViewById<Button>(R.id.editBackBtn)
        deleteButton.setOnClickListener {
            // 사용자 정보를 삭제하는 함수 호출
            deleteUser(userEmail)
        }

        // 홈 버튼 클릭 리스너 설정
        val homeImageView = findViewById<ImageView>(R.id.homeImageViewUserEdit)
        homeImageView.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        // 지도 버튼 클릭 리스너 설정
        val mapFloatingActionButton = findViewById<FloatingActionButton>(R.id.mapFabUserEdit)
        mapFloatingActionButton.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v != null && v is android.widget.EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    //사용자 조회
    private fun getUserInfo(email: String) {
        // 세션에서 이메일을 가져와서 사용자 정보 요청
        userService.getUserInfo(email).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        // 사용자 정보가 있을 경우 EditText에 설정
                        nameEditText.setText(user.name)
                        addressEditText.setText(user.address)
                        telEditText.setText(user.phoneNumber)
                        birthEditText.setText(user.birthDate)
                        // 로그에 값을 출력
                        Log.d(
                            "UserInfo",
                            "Name: ${user.name}, Address: ${user.address}, Phone: ${user.phoneNumber}, BirthDate: ${user.birthDate}"
                        )
                    } else {
                        // 사용자 정보가 없을 경우 예외 처리
                        Log.d("UserInfo", "User object is null")
                    }
                } else {
                    // 서버로부터 응답이 실패했을 경우 예외 처리
                    Log.d("UserInfo", "Failed to fetch user info: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                // 네트워크 오류 등의 이유로 요청이 실패한 경우 예외 처리
                Log.e("UserInfo", "Failed to fetch user info", t)
                Toast.makeText(
                    this@UserEditActivity,
                    "Failed to fetch user info",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }



    // 회원 정보 수정 함수
    private fun userUpdate() {

        val newName = nameEditText.text.toString()
        val newAddress = addressEditText.text.toString()
        val newTel = telEditText.text.toString()
        val newBirth = birthEditText.text.toString()
        val addRole = listOf("ROLE_USER")

        // 수정된 사용자 정보 생성
        var updatedUser =
            User(id = 0, email = "12", name = newName, password = "12", gender = "male", joinDate= "20240401", address = newAddress, phoneNumber = newTel, birthDate = newBirth, role = addRole)
        Log.d(">>", "${updatedUser}")
        val userService = RetrofitInstance.userService
        userService.userUpdate(updatedUser).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    // 성공적으로 수정되었을 때의 처리
                    Log.d("성공", "성공성공")
                } else {
                    // 수정에 실패했을 때의 처리
                    val errorCode = response.code()
                    Log.d("실패", "오류 코드: $errorCode")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                // 통신 실패 처리
                Log.d("통신실패", "통신실패실패")
            }
        })

    }

    // 회원 정보 삭제
    // 사용자 삭제 메서드
    private fun deleteUser(email: String) {
        userService.deleteUser(email).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    // 삭제 요청이 성공한 경우 로그인 화면으로 이동합니다.
                    Log.d("삭제성공", "굿굿굿")
                    val intent = Intent(this@UserEditActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish() // 현재 화면 종료
                } else {
                    Log.d("삭제실패", "삭제안됨")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // 통신 실패 처리
                Log.d("통신실패", "통신실패 => 삭제안됨")
            }
        })
    }

}