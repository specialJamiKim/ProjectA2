package com.example.projecta2.View

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.example.projecta2.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class ReservationActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation)

        // 액션바 숨기기
        supportActionBar?.hide()

        // 홈 버튼 클릭 리스너 설정
        val homeImageView = findViewById<ImageView>(R.id.homeImageViewUserEdit2)
        homeImageView.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        // 지도 버튼 클릭 리스너 설정
        val mapFloatingActionButton = findViewById<FloatingActionButton>(R.id.mapFabUserEdit2)
        mapFloatingActionButton.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }

        // 마이페이지 버튼 클릭 리스너 설정
        val myPageImageView = findViewById<ImageView>(R.id.myPageImageView2)
        myPageImageView.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }

        // 여기에 Spinner(헬스장 선택), DatePicker, 로직을 구현
        // 예시: 날짜 선택
        val datePicker = findViewById<DatePicker>(R.id.datePicker)
        val today = Calendar.getInstance()
        datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH)) { view, year, month, day ->
            // 날짜가 선택되었을 때의 처리
        }


        // "예약하기" 버튼 클릭 이벤트
        val btnReserve = findViewById<Button>(R.id.btnReserve)
        btnReserve.setOnClickListener {
            // 예약 로직 구현
        }
    }
}
