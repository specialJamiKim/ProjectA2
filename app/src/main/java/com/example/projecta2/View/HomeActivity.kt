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
    private lateinit var favorite: FloatingActionButton
    private lateinit var cactusCardView: CardView

    private lateinit var wannaGymCardView: CardView


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        personLinearLayout = findViewById(R.id.person_linear_layout)
        favorite = findViewById(R.id.favorite)
        cactusCardView = findViewById(R.id.cactus_card_view)
        wannaGymCardView = findViewById(R.id.wannaGym_card_view)

        personLinearLayout.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }

        favorite.setOnClickListener {
            // 여기서 원하는 동작을 구현하세요, 예를 들면 지도 보여주기 등
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }

        cactusCardView.setOnClickListener {
            val intent = Intent(this, CenterDetailActivity::class.java).apply {
                putExtra("itemName1", "비나이더짐")
                putExtra("itemPrice1", "15,000원")
            }
            startActivity(intent)
        }

        wannaGymCardView.setOnClickListener {
            val intent = Intent(this, CenterDetailActivity::class.java).apply {
                putExtra("itemName2", "워너짐 수영구점")
                putExtra("itemPrice2", "10,000원")
            }
            startActivity(intent)
        }


    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
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
