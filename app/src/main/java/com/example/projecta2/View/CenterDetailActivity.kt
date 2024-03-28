package com.example.projecta2.View

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.projecta2.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CenterDetailActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_center_detail)

        // 액션 바 숨기기
        supportActionBar?.hide()

        // 홈 버튼 클릭 리스너 설정
        val homeImageView = findViewById<ImageView>(R.id.centerToHome)
        homeImageView.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        // 지도 버튼 클릭 리스너 설정
        val mapFloatingActionButton = findViewById<FloatingActionButton>(R.id.centerToMap)
        mapFloatingActionButton.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }

        // 마이페이지 버튼 클릭 리스너 설정
        val myPageImageView = findViewById<ImageView>(R.id.centerToMypage)
        myPageImageView.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }

        // Intent에서 데이터 추출
        val itemName = intent.getStringExtra("itemName1") // 이름
        val itemPrice = intent.getStringExtra("itemPrice1") // 가격

        // TextView 찾기
        val textViewItemName = findViewById<TextView>(R.id.centerName) // 이름을 표시할 TextView
        val textViewItemPrice = findViewById<TextView>(R.id.centerPrice) // 가격을 표시할 TextView

        // TextView에 데이터 설정
        textViewItemName.text = itemName
        textViewItemPrice.text = itemPrice

        // "뒤로 가기" 버튼 클릭 이벤트 처리
        val backButton = findViewById<Button>(R.id.buttonBack)
        backButton.setOnClickListener {
            // HomeActivity로 이동
            val intent = Intent(this@CenterDetailActivity, HomeActivity::class.java)
            startActivity(intent)
            finish() // 현재 액티비티 종료
        }

        // "예약하기" 버튼 찾기 및 클릭 이벤트 처리
        val reserveButton = findViewById<Button>(R.id.centerReservBtn)
        reserveButton.setOnClickListener {
            // ReservationActivity로 이동
            val intent = Intent(this, ReservationActivity::class.java)
            startActivity(intent)
        }
    }
}
