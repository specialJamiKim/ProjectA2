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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projecta2.Dao.UserDB
import com.example.projecta2.R
import com.example.projecta2.model.User
import com.example.projecta2.adapter.MypageAdapter // 어댑터 임포트 추가
import com.example.projecta2.model.FitnessCenter
import com.example.projecta2.model.Reservation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyPageActivity : AppCompatActivity() {

    // Room Database 인스턴스
    private lateinit var db: UserDB
    private var userName: String? = null
    private lateinit var tvUserName: TextView
    private lateinit var recyclerView: RecyclerView // 리사이클러뷰 선언

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        // 데이터베이스 사용 준비
        db = DatabaseInitializer.initDatabase(this)
        tvUserName = findViewById<TextView>(R.id.MyPageUserName)

        // 사용자 이메일 가져오기
        val email = SessionManager.getUserEmail(this)
        if (email != null) {
            Log.d("Email Log", email)
            // 코루틴 사용하여 DB 작업 실행
            lifecycleScope.launch {
                val stUser = withContext(Dispatchers.IO) {
                    db.getDao().getUserInfoObj(email)
                }
                userName = stUser?.name
                tvUserName.text = userName
                Log.d("로그인 상태", "현재 로그인된 ${userName}님 고유 id 는 ${stUser?.Id} 입니다")
            }
        } else {
            Log.d("Email Log", "Email is null")
        }

        // 홈으로 이동
        val homeLinearLayout = findViewById<LinearLayout>(R.id.home_linear_layout)
        homeLinearLayout.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        // 맵 보기
        val showMap = findViewById<FloatingActionButton>(R.id.favorite)
        showMap.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }

        // 사용자 정보 수정 페이지 이동
        val editInfoCard = findViewById<CardView>(R.id.editInfo)
        editInfoCard.setOnClickListener {
            val intent = Intent(this, UserEditActivity::class.java)
            startActivity(intent)
        }

        // 리사이클러뷰 설정
        recyclerView = findViewById<RecyclerView>(R.id.myPageRecycler) // 리사이클러뷰 ID 할당
        recyclerView.layoutManager = LinearLayoutManager(this) // 리사이클러뷰 레이아웃 매니저 설정
        // 여기에 어댑터 설정 코드 추가. 예시를 위한 가상의 데이터 리스트와 어댑터 설정을 진행합니다.

        // 실제 데이터 리스트를 DB나 서버에서 가져와야 합니다.
        // val exampleList = listOf()

        // 리사이클러뷰 어댑터 설정
        //recyclerView.adapter = MypageAdapter(exampleList) // 어댑터 설정
    }
}
