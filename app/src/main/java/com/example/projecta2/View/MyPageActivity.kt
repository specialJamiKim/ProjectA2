
package com.example.projecta2.View

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.cardview.widget.CardView
import com.example.projecta2.R
import com.example.projecta2.View.HomeActivity
import com.example.projecta2.View.MapActivity
import com.example.projecta2.View.UserEditActivity

class MyPageActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

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
            // UserInfoEditActivity가 사용자 정보 수정 페이지를 나타내는 액티비티라고 가정합니다.
            val intent = Intent(this, UserEditActivity::class.java)
            startActivity(intent)
        }
    }
}
