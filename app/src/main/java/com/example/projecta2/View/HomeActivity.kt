package com.example.projecta2.View

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.projecta2.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeActivity : AppCompatActivity() {

    private lateinit var personLinearLayout: LinearLayout
    private lateinit var showMap: FloatingActionButton
    private lateinit var cactusCardView: CardView
    private lateinit var btnDayPrice: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // 뷰 바인딩
        personLinearLayout = findViewById(R.id.person_linear_layout)
        showMap = findViewById(R.id.showMap)
        cactusCardView = findViewById(R.id.cactus_card_view)



        // 각 뷰의 클릭 이벤트 설정
        personLinearLayout.setOnClickListener {
            // MyPageActivity로 이동
            val intent = Intent(applicationContext, MyPageActivity::class.java)
            startActivity(intent)
        }

        showMap.setOnClickListener {
            // MapActivity로 이동
            val intent = Intent(applicationContext, MapActivity::class.java)
            startActivity(intent)
        }

        cactusCardView.setOnClickListener {
            // CenterDetailActivity로 이름과 가격 정보 전달
            val intent = Intent(applicationContext, CenterDetailActivity::class.java)
            intent.putExtra("itemName1", "비나이더짐") // 이름 데이터 추가
            intent.putExtra("itemPrice1", "15,000원") // 가격 데이터 추가
            startActivity(intent)
        }


    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        // 화면 터치 시 키보드 숨김 처리
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is android.widget.EditText) {
                v.clearFocus()
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }
        }
        return super.dispatchTouchEvent(event)
    }
}
