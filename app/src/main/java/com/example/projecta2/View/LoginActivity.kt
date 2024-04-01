package com.example.projecta2.View

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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

    // 구글 로그인
    private fun googleSignIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
    }

    // 일반 로그인
    private fun signIn(email: String, password: String) {
        val userService = RetrofitInstance.userService
        userService.signIn(email, password).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    response.body()?.let { user ->
                        Log.d("LoginSuccess", "Email: ${user.email}, Name: ${user.name}")
                        val intent = Intent(applicationContext, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } ?: run {
                        Log.e("ResponseError", "Received null user object")
                    }
                } else {
                    Log.e("ResponseError", "Code: ${response.code()} , 서버 연결 실패")
                    DialogHelper.showMessageDialog(this@LoginActivity, "Login Fail", "없는 회원입니다.\n아이디와 비밀번호를 확인해주세요.")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("RequestFailed", "Error: ${t.message}", t)
            }
        })
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

    // 구글 로그인 결과 핸들링
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            Log.d("GoogleLoginSuccess", "이메일: ${account.email}, 성: ${account.familyName}, 이름: ${account.givenName}, 전체 이름: ${account.displayName}, 프로필 사진 URL: ${account.photoUrl}")
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
            finish()
        } catch (e: ApiException) {
            Log.w("GoogleLoginFailure", "signInResult:실패 code=${e.statusCode}", e)
        }
    }
}
