package com.example.projecta2.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.projecta2.R
import com.example.projecta2.adapter.ReviewAdapter
import com.example.projecta2.model.Review
import com.example.projecta2.util.RetrofitInstance
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CenterDetailActivity : AppCompatActivity() {
    private lateinit var reviewRecyclerView: RecyclerView
    private lateinit var reviewAdapter: ReviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_center_detail)

        supportActionBar?.hide() // 액션바 숨기기

        // UI 컴포넌트 초기화
        val homeImageView: ImageView = findViewById(R.id.centerToHome)
        val mapFloatingActionButton: FloatingActionButton = findViewById(R.id.centerToMap)
        val myPageImageView: ImageView = findViewById(R.id.centerToMypage)
        val textViewItemName: TextView = findViewById(R.id.centerName)
        val textViewItemPrice: TextView = findViewById(R.id.centerPrice)
        val textViewItemAddress: TextView = findViewById(R.id.centeraddress)
        val textViewCenterText: TextView = findViewById(R.id.centerText)
        val imageView: ImageView = findViewById(R.id.centerImg)
        val backButton: Button = findViewById(R.id.buttonBack)
        val reserveButton: Button = findViewById(R.id.centerReservBtn)

        // 리사이클러뷰 및 어댑터 설정
        reviewRecyclerView = findViewById(R.id.reviewRecyclerView)
        reviewRecyclerView.layoutManager = LinearLayoutManager(this)
        reviewAdapter = ReviewAdapter(emptyList()) // 빈 리스트로 어댑터 초기화
        reviewRecyclerView.adapter = reviewAdapter // 어댑터를 리사이클러뷰에 설정

        // Intent에서 데이터 추출
        val centerName = intent.getStringExtra("centerName") ?: ""
        val centerPrice = intent.getLongExtra("centerPrice", 0L)
        val centerLocation = intent.getStringExtra("centerLocation") ?: ""
        val centerImageUrl = intent.getStringExtra("centerImageUrl") ?: ""

        // 데이터 설정
        textViewItemName.text = centerName
        textViewItemPrice.text = "${centerPrice}원"
        textViewItemAddress.text = centerLocation
        textViewCenterText.text = getCenterDescription(centerLocation, centerPrice)

        // 이미지 로딩
        Glide.with(this)
            .load(centerImageUrl)
            .apply(RequestOptions()
                .placeholder(R.drawable.bannerimg)
                .error(R.drawable.bannerimg2)
                .diskCacheStrategy(DiskCacheStrategy.ALL))
            .into(imageView)

        // 클릭 리스너 설정
        setClickListeners(homeImageView, mapFloatingActionButton, myPageImageView, backButton, reserveButton, centerName)

        // 리뷰 데이터 요청 및 처리
        fetchReviews()
    }

    private fun setClickListeners(homeImageView: ImageView, mapFloatingActionButton: FloatingActionButton, myPageImageView: ImageView, backButton: Button, reserveButton: Button, centerName: String) {
        homeImageView.setOnClickListener { startActivity(Intent(this, HomeActivity::class.java)) }
        mapFloatingActionButton.setOnClickListener { startActivity(Intent(this, MapActivity::class.java)) }
        myPageImageView.setOnClickListener { startActivity(Intent(this, MyPageActivity::class.java)) }
        backButton.setOnClickListener { finish() }
        reserveButton.setOnClickListener {
            val intent = Intent(this, ReservationActivity::class.java).apply {
                putExtra("centerName", centerName)
            }
            startActivity(intent)
        }
    }

    private fun getCenterDescription(location: String, price: Long): String =
        """
        $location 에 자리잡은 저희 헬스장은 최상의 시설과 탁월한 관리로 여러분의 만족을 최우선으로 합니다.
        일일권 $price 원의 가격으로 친절한 직원들과, 깨끗하고 넓은 시설을 체험해 보세요.
        다양한 최신 운동 기구와 넓은 공간에서 여러분만의 운동 루틴도 만들어 보세요.
        """.trimIndent()

    private fun fetchReviews() {
        RetrofitInstance.reviewService.getAllReviews().enqueue(object : Callback<List<Review>> {
            override fun onResponse(call: Call<List<Review>>, response: Response<List<Review>>) {
                if (response.isSuccessful) {
                    // 성공적으로 리뷰 데이터를 받아온 경우 어댑터에 데이터 업데이트
                    reviewAdapter.updateData(response.body() ?: emptyList())
                } else {
                    Log.e("CenterDetailActivity", "Server Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Review>>, t: Throwable) {
                Log.e("CenterDetailActivity", "Network Error: ${t.message}")
            }
        })
    }
}
