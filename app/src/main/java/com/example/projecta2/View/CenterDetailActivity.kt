package com.example.projecta2.View

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import com.example.projecta2.model.Reservation
import com.example.projecta2.model.Review
import com.example.projecta2.util.DialogHelper
import com.example.projecta2.util.RetrofitInstance
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.util.Calendar
import kotlin.properties.Delegates

class CenterDetailActivity : AppCompatActivity() {

    private lateinit var db: UserDB
    private lateinit var reviewView: RecyclerView
    private lateinit var submitReviewBtn: Button
    private lateinit var reviewEditText: EditText
    private lateinit var userInfo: UserInfo
    private lateinit var fitnessCenter: FitnessCenter
    private var centerId by Delegates.notNull<Long>()
    private var selectedDate: String? = null
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
        centerId = intent.getLongExtra("centerId", 0L)
        fitnessCenter = FitnessCenter(id = centerId)
        // Intent에서 데이터 추출 => userInfo는 parcelable 처리하여 이렇게 사용해야함
        userInfo = intent.getParcelableExtra<UserInfo>("userInfo")!!

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
            showDatePicker()
        }
    }

   /* private fun addReview() {
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
    }*/

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
                        RetrofitInstance.reviewService.addReview(review).enqueue(object :
                            Callback<ResponseBody> {
                            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
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
                            }

                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                // 실패 시 메시지 출력 또는 다른 처리
                                Toast.makeText(this@CenterDetailActivity, "리뷰 등록에 실패했습니다.", Toast.LENGTH_SHORT)
                                    .show()
                                Log.e("Add Review", "Failed to add review: ${t.message}")
                            }
                        })
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


    //센터 예약 달력 함수
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, dayOfMonth ->
                selectedDate = "$selectedYear-${selectedMonth + 1}-$dayOfMonth"
                showReservationConfirmationDialog(selectedDate!!)
            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.show()
    }

    //센터 예약 함수
    private fun showReservationConfirmationDialog(reservationDate: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("예약 확인")
        val message = "예약을 진행하시겠습니까?\n\n예약 날짜: $reservationDate"
        builder.setMessage(message)
        Log.d("예약일", "${reservationDate}")
        builder.setPositiveButton("예약") { dialog, _ ->
            DialogHelper.showMessageDialog(this, "예약 확인", "예약이 완료되었습니다.\n\n예약 날짜: $reservationDate") {

                //서버 DB에 예약 정보 보내는 부분
                serverDbSaveReservation(userInfo,fitnessCenter,reservationDate)

                // 예약 완료 후 마이페이지로 이동
                val intent = Intent(this, MyPageActivity::class.java)
                startActivity(intent)
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("취소") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun serverDbSaveReservation(userInfo : UserInfo , fitnessCenter: FitnessCenter, reservationDate: String){
        //예약객체하나 만들기
        val reservationObj = Reservation(center = fitnessCenter, user = userInfo.toUser(), reservationTime = reservationDate )

        val reservationService = RetrofitInstance.reservationService
        reservationService.createReservation(reservationObj).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("예약성공", "DB확인해보세요 있음 ㄹㅇㅋㅋ")
                } else {
                    // 예약 생성이 실패했을 때 처리하는 코드
                    Log.e("Reservation Error", "Failed to create reservation. Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // 요청이 실패했을 때 처리하는 코드
                Log.e("Reservation Error", "Failed to create reservation. Error: ${t.message}", t)
            }
        })

    }
}
