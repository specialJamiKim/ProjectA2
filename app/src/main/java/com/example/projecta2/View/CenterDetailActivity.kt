package com.example.projecta2.View

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.projecta2.R

class CenterDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_center_detail)


        // Intent에서 데이터 추출
        val itemName = intent.getStringExtra("itemName1") // 이름
        val itemPrice = intent.getStringExtra("itemPrice1") // 가격

        // TextView 찾기
        val textViewItemName = findViewById<TextView>(R.id.textViewItemName) // 이름을 표시할 TextView
        val textViewItemPrice = findViewById<TextView>(R.id.textViewItemPrice) // 가격을 표시할 TextView

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
            // "예약하기" 버튼 찾기
            val reserveButton = findViewById<Button>(R.id.buttonReserve)
            // "예약하기" 버튼 클릭 이벤트 처리
            reserveButton.setOnClickListener {
                // ReservationActivity로 이동
                val intent = Intent(this, ReservationActivity::class.java)
                startActivity(intent)
        }
    }
}
