package com.example.projecta2.View

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.projecta2.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CenterDetailActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_center_detail)

        //액션바 숨기기
        supportActionBar?.hide()

        // UI 컴포넌트 초기화
        val homeImageView = findViewById<ImageView>(R.id.centerToHome)
        val mapFloatingActionButton = findViewById<FloatingActionButton>(R.id.centerToMap)
        val myPageImageView = findViewById<ImageView>(R.id.centerToMypage)
        val textViewItemName = findViewById<TextView>(R.id.centerName)
        val textViewItemPrice = findViewById<TextView>(R.id.centerPrice)
        val textViewItemAddress = findViewById<TextView>(R.id.centeraddress)
        val textViewCenterText = findViewById<TextView>(R.id.centerText)
        val imageView = findViewById<ImageView>(R.id.centerImg)
        val backButton = findViewById<Button>(R.id.buttonBack)
        val reserveButton = findViewById<Button>(R.id.centerReservBtn)

        // Intent에서 데이터 추출
        val itemName = intent.getStringExtra("itemName1")
        val itemPrice = intent.getStringExtra("itemPrice1")
        val itemAddress = intent.getStringExtra("itemAddress1")
        val imageUrl = intent.getStringExtra("itemImageUrl")

        // 데이터 설정
        textViewItemName.text = itemName
        textViewItemPrice.text = itemPrice
        textViewItemAddress.text = itemAddress
        textViewCenterText.text = "$itemAddress 에 자리잡은 저희 헬스장은 최상의 시설과 탁월한 관리로 여러분의 만족을 최우선으로 합니다.\n일일권 $itemPrice 원의 가격으로 친절한 직원들과, 깨끗하고 넓은 시설을 체험해 보세요.\n다양한 최신 운동 기구와 넓은 공간에서 여러분만의 운동 루틴도 만들어 보세요."

        // 클릭 리스너 설정
        homeImageView.setOnClickListener { startActivity(Intent(this, HomeActivity::class.java)) }
        mapFloatingActionButton.setOnClickListener { startActivity(Intent(this, MapActivity::class.java)) }
        myPageImageView.setOnClickListener { startActivity(Intent(this, MyPageActivity::class.java)) }
        backButton.setOnClickListener { finish() }
        reserveButton.setOnClickListener { startActivity(Intent(this, ReservationActivity::class.java)) }

        // 이미지 로딩
        imageUrl?.let {
            if (it.isNotEmpty()) {
                Glide.with(this)
                    .load(it)
                    .apply(RequestOptions()
                        .placeholder(R.drawable.bannerimg)
                        .error(R.drawable.bannerimg2)
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(imageView)
            } else {
                imageView.setImageResource(R.drawable.favorite_img_7) // 기본 이미지 설정
            }
        }
    }
}
