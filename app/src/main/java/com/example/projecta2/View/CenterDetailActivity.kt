package com.example.projecta2.View

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.projecta2.Dao.UserDB
import com.example.projecta2.Entity.UserInfo
import com.example.projecta2.R
import com.example.projecta2.model.FitnessCenter
import com.example.projecta2.model.Review
import com.example.projecta2.util.RetrofitInstance
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class CenterDetailActivity : AppCompatActivity() {

    private lateinit var db: UserDB
    private lateinit var reviewView: RecyclerView
    private lateinit var submitReviewBtn: Button
    private lateinit var reviewEditText: EditText
    private lateinit var user: UserInfo
    private lateinit var fitnessCenter: FitnessCenter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_center_detail)

        //reviewView = findViewById(R.id.reviewView)
        reviewEditText = findViewById(R.id.reviewEditText)
        submitReviewBtn = findViewById(R.id.submitReviewBtn)
        submitReviewBtn.setOnClickListener {
            addReview()
        }

        // 액션바 숨기기
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
        val centerName = intent.getStringExtra("centerName")
        val centerPrice = intent.getLongExtra("centerPrice", 0L) // Long 타입으로 받음
        val centerLocation = intent.getStringExtra("centerLocation")
        val centerImageUrl = intent.getStringExtra("centerImageUrl")

        // 추가부분 => centerId 가져오기
        val centerId = intent.getLongExtra("centerId", 0L)
        // Intent에서 데이터 추출 => userInfo는 parcelable 처리하여 이렇게 사용해야함
        val userInfo = intent.getParcelableExtra<UserInfo>("userInfo")
        Log.d("UserInfo", "UserInfo: $userInfo")
        Log.d("CenterId", "CenterId: $centerId")

        // 데이터 설정
        textViewItemName.text = centerName
        textViewItemPrice.text = "${centerPrice}원"
        textViewItemAddress.text = centerLocation
        textViewCenterText.text = """
            $centerLocation 에 자리잡은 저희 헬스장은 최상의 시설과 탁월한 관리로 여러분의 만족을 최우선으로 합니다.
            일일권 ${centerPrice}원의 가격으로 친절한 직원들과, 깨끗하고 넓은 시설을 체험해 보세요.
            다양한 최신 운동 기구와 넓은 공간에서 여러분만의 운동 루틴도 만들어 보세요.
        """.trimIndent()

        // 이미지 로딩
        Glide.with(this)
            .load(centerImageUrl)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.bannerimg)
                    .error(R.drawable.bannerimg2)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .into(imageView)

        // 클릭 리스너 설정
        homeImageView.setOnClickListener { startActivity(Intent(this, HomeActivity::class.java)) }
        mapFloatingActionButton.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    MapActivity::class.java
                )
            )
        }
        myPageImageView.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    MyPageActivity::class.java
                )
            )
        }
        backButton.setOnClickListener { finish() }
        reserveButton.setOnClickListener {
            val intent = Intent(this, ReservationActivity::class.java).apply {
                putExtra("centerName", centerName)
                // 추가 데이터 전달이 필요한 경우 여기에 추가
            }
            startActivity(intent)
        }
    }

    private fun addReview() {
        val reviewText = reviewEditText.text.toString()
        if (reviewText.isNotEmpty()) {
            lifecycleScope.launch {
                try {
                    // 현재 사용자와 센터 ID를 Intent에서 가져옴
                    val centerId = intent.getLongExtra("centerId", 0L)

                    // 사용자 정보 생성
                    val userInfo = intent.getParcelableExtra<UserInfo>("userInfo")
                    val userId = userInfo?.Id

                    // Review 객체 생성
                    val review = Review(
                        userId = userId,
                        centerId = centerId, // 센터 ID만 사용하여 객체 생성
                        rating = null, // 선택된 평점
                        reviewText = reviewText
                    )

                    // Retrofit을 사용하여 리뷰 추가 요청 수행
                    val response = withContext(Dispatchers.IO) {
                        RetrofitInstance.reviewService.addReview(review).execute()
                    }

                    // 요청이 성공적으로 수행되었는지 여부 확인
                    if (response.isSuccessful) {
                        // 성공적으로 추가되면 리뷰 목록을 다시 불러옴

                        Toast.makeText(
                            this@CenterDetailActivity,
                            "리뷰가 성공적으로 등록되었습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // 실패 시 메시지 출력 또는 다른 처리
                        Toast.makeText(
                            this@CenterDetailActivity,
                            "리뷰 등록에 실패했습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: HttpException) {
                    // HTTP 요청 실패 시 처리
                    Toast.makeText(this@CenterDetailActivity, "리뷰 등록에 실패했습니다.", Toast.LENGTH_SHORT)
                        .show()
                    Log.e("Add Review", "Failed to add review: ${e.message()}")
                } catch (e: Throwable) {
                    // 그 외 에러 처리
                    Toast.makeText(this@CenterDetailActivity, "리뷰 등록에 실패했습니다.", Toast.LENGTH_SHORT)
                        .show()
                    Log.e("Add Review", "Failed to add review: ${e.message}")
                }
            }
        } else {
            Toast.makeText(this, "리뷰를 작성해주세요.", Toast.LENGTH_SHORT).show()
        }
    }
}
