package com.example.projecta2.View


import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.LinearLayout
import androidx.lifecycle.lifecycleScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.cardview.widget.CardView
import com.example.projecta2.Dao.UserDB
import com.example.projecta2.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyPageActivity : AppCompatActivity() {

    // Room Database instance
    private lateinit var db: UserDB
    private var userName: String? = null // 타입을 String?으로 변경하고 초기값을 null로 설정
    private lateinit var tvUserName: TextView
    override fun onCreate(savedInstanceState: Bundle?) { // 'override' 오타 수정
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)
        // DB 사용 준비
        db = DatabaseInitializer.initDatabase(this)
        tvUserName = findViewById<TextView>(R.id.MyPageUserName)


        val email = SessionManager.getUserEmail(this) // toString() 호출 제거
        if (email != null) { // email이 null인 경우를 안전하게 처리
            Log.d("Email Log", email) // 실제 로그 메시지를 명확하게 변경
            // 코루틴을 사용하여 데이터베이스 작업을 백그라운드 스레드에서 실행
            lifecycleScope.launch {
                val stUser = withContext(Dispatchers.IO) {
                    db.getDao().getUserInfoObj(email)
                }
                // 유저 이름 저장
                userName = stUser?.name
                tvUserName.text = userName
                // 메인 스레드에서 로그 출력
                Log.d("로그인 상태", "현재 로그인된 ${userName}님 고유 id 는 ${stUser?.Id} 입니다")
            }
        } else {
            Log.d("Email Log", "Email is null")
        }

        val homeLinearLayout = findViewById<LinearLayout>(R.id.home_linear_layout)
        homeLinearLayout.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        val showMap = findViewById<FloatingActionButton>(R.id.favorite)
        showMap.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }

        // 사용자 정보 수정 페이지로 이동하는 기능
        val editInfoCard = findViewById<CardView>(R.id.editInfo)
        editInfoCard.setOnClickListener {
            val intent = Intent(this, UserEditActivity::class.java)
            startActivity(intent)
        }
    }
}
