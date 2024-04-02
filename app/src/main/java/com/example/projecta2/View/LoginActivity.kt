package com.example.projecta2.View

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.projecta2.Dao.UserDB
import com.example.projecta2.Entity.UserInfo
import com.example.projecta2.R
import com.example.projecta2.model.User
import com.example.projecta2.util.DialogHelper
import com.example.projecta2.util.RetrofitInstance
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var login: Button
    private lateinit var btnJoinPage: Button
    private lateinit var btnGoogleLogin: Button
    private lateinit var userEmailTextView: EditText
    private lateinit var userPwTextView: EditText
    private lateinit var email: String
    private lateinit var password: String

    // 구글 로그인 관련
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var resultLauncher: ActivityResultLauncher<Intent>

    // Room Database instance
    lateinit var db: UserDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        db = DatabaseInitializer.initDatabase(this)
        // Room 데이터베이스 초기화
        deleteAllUsers()

        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            Log.d("login", "이미 로그인 되어있음")
        } else {
            Log.d("login not yet", "로그인 되어있지 않음")
        }

        // 구글 로그인 초기화
        setResultSignUp()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail().requestProfile().build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        // 뷰 바인딩
        btnGoogleLogin = findViewById(R.id.btnGoogleLogin)
        btnJoinPage = findViewById(R.id.btnJoinPage)
        userEmailTextView = findViewById(R.id.userEmailTextView)
        userPwTextView = findViewById(R.id.userPwTextView)
        login = findViewById(R.id.login)

        // 로그인 버튼 클릭 리스너
        login.setOnClickListener {
            email = userEmailTextView.text.toString()
            password = userPwTextView.text.toString()
            SessionManager.saveUserEmail(this, email)
            signIn(email, password)
        }

        // 회원가입 페이지 이동
        btnJoinPage.setOnClickListener {
            val intent = Intent(applicationContext, JoinActivity::class.java)
            startActivity(intent)
        }

        // 구글 로그인 버튼 클릭 리스너
        btnGoogleLogin.setOnClickListener {
            Log.d("GoogleLogin", "구글 로그인 버튼 클릭")
            googleSignIn()
        }
    }

    // 데이터베이스에서 모든 사용자 삭제
    private fun deleteAllUsers() {
        Thread {
            db.getDao().deleteAllUsers()
        }.start()
    }

    // 구글 로그인
    private fun googleSignIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
    }

    // 구글 로그인 결과 처리
    private fun setResultSignUp() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    handleSignInResult(task)
                }
            }
    }

    //
    private fun saveToRoomDB(account: GoogleSignInAccount) {
        // Room을 사용하여 내장 DB에 구글 계정 정보를 저장하는 작업 수행
        val userInfo = UserInfo(
            Id = null,
            email = account.email,
            name = account.displayName,
            password = null, // 비밀번호는 구글 로그인 시 필요하지 않음
            phoneNumber = null,
            gender = null,
            address = null,
            joinDate = null,
            role = emptyList(),
            birthDate = null
        )
        Thread {
            db.getDao().insertUser(userInfo)
        }.start()
    }

    // 구글 로그인 결과 핸들링
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            saveToRoomDB(account) // Room 데이터베이스에 사용자 정보 저장

            Log.d("GoogleLoginSuccess", "이메일: ${account.email}, 성: ${account.familyName}, 이름: ${account.givenName}, 전체 이름: ${account.displayName}, 프로필 사진 URL: ${account.photoUrl}")
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
            finish()
        } catch (e: ApiException) {
            Log.w("GoogleLoginFailure", "signInResult:실패 code=${e.statusCode}", e)
        }
    }




    // 일반 로그인
    private fun signIn(email: String, password: String) {
        val userService = RetrofitInstance.userService
        userService.signIn(email, password).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    response.body()?.let { user ->
                        Log.d("LoginSuccess", "이메일: ${user.email}, 이름: ${user.name}")

                        // 사용자 정보를 Room 데이터베이스에 저장하고 저장된 정보를 로그로 출력
                        Thread {
                            val userInfo = UserInfo(
                                Id = user.id,
                                email = user.email,
                                name = user.name,
                                password = password,
                                phoneNumber = user.phoneNumber,
                                gender = user.gender,
                                address = user.address,
                                joinDate = user.joinDate,
                                role = user.role,
                                birthDate = user.birthDate
                            )

                            db.getDao().insertUser(userInfo)

                            // 저장된 비밀번호를 가져와서 로그로 출력
                            val stUser = db.getDao().getUserInfoObj(email)

                            Log.d("StoredUserInfo", "${stUser}")



                            runOnUiThread {
                                val intent = Intent(applicationContext, HomeActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }.start()
                    } ?: run {
                        Log.e("ResponseError", "null 사용자 객체를 받았습니다.")
                    }
                } else {
                    Log.e("ResponseError", "코드: ${response.code()}, 서버 연결 실패")
                    DialogHelper.showMessageDialog(this@LoginActivity, "로그인 실패", "회원 정보가 없습니다.\n이메일과 비밀번호를 확인해주세요.")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("RequestFailed", "오류: ${t.message}", t)
            }
        })
    }
}
