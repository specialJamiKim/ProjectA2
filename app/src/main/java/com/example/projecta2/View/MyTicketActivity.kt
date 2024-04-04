package com.example.projecta2.View

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projecta2.Entity.UserInfo
import com.example.projecta2.R
import com.example.projecta2.model.Reservation
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MyTicketActivity : AppCompatActivity() {

    private lateinit var userInfo: UserInfo

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_ticket)

        // 사용자 정보를 인텐트로부터 가져옵니다.
        userInfo = intent.getParcelableExtra<UserInfo>("userInfo")!!

        // 사용자 이름을 화면에 표시합니다.
        val ticketPageUserName: TextView = findViewById(R.id.ticketPageUserName)
        ticketPageUserName.text = userInfo.name

        // 시스템 바의 Insets를 적용합니다.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home_linear_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            WindowInsetsCompat.CONSUMED
        }

        // 홈 버튼 클릭 리스너 설정
        val homeImageView: ImageView = findViewById(R.id.homeImageViewUserEdit4)
        homeImageView.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        // 지도 버튼 클릭 리스너 설정
        val mapFloatingActionButton: FloatingActionButton = findViewById(R.id.mapFabUserEdit4)
        mapFloatingActionButton.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }

        // 마이페이지 버튼 클릭 리스너 설정
        val myPageImageView: ImageView = findViewById(R.id.myPageImageView4)
        myPageImageView.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }

        // 리사이클러뷰 설정
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.ticketUseRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 예시 데이터 리스트 (실제 앱에서는 서버 또는 데이터베이스에서 데이터를 로드해야 합니다)
        val reservationList = listOf<Reservation>() // 여기에 데이터를 채워넣으세요.

        recyclerView.adapter = MyTicketAdapter(reservationList)
    }
}

