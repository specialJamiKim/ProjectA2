package com.example.projecta2.View

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projecta2.Dao.UserDB
import com.example.projecta2.Entity.UserInfo
import com.example.projecta2.R
import com.example.projecta2.adapter.ReviewAdapter
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
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var submitReviewBtn: Button
    private lateinit var reviewEditText: EditText
    private lateinit var userInfo: UserInfo
    private lateinit var fitnessCenter: FitnessCenter
    private var centerId by Delegates.notNull<Long>()
    private var selectedDate: String? = null
    private lateinit var reviews: List<Review>
    private lateinit var ratingBar: RatingBar

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_center_detail)

        reviewView = findViewById(R.id.reviewView)
        reviewEditText = findViewById(R.id.reviewEditText)
        submitReviewBtn = findViewById(R.id.submitReviewBtn)
        submitReviewBtn.setOnClickListener {
            addReview()
        }
        ratingBar = findViewById(R.id.ratingBar)

        supportActionBar?.hide()

        val homeImageView = findViewById<ImageView>(R.id.centerToHome)
        val mapFloatingActionButton = findViewById<FloatingActionButton>(R.id.centerToMap)
        val myPageImageView = findViewById<ImageView>(R.id.centerToMypage)
        val textViewItemName = findViewById<TextView>(R.id.centerName)
        val textViewRating = findViewById<TextView>(R.id.centerRating)
        val textViewItemPrice = findViewById<TextView>(R.id.centerPrice)
        val textViewItemAddress = findViewById<TextView>(R.id.centeraddress)
        val textViewCenterText = findViewById<TextView>(R.id.centerText)
        val imageView = findViewById<ImageView>(R.id.centerImg)
        val backButton = findViewById<Button>(R.id.buttonBack)
        val reserveButton = findViewById<Button>(R.id.centerReservBtn)

        centerId = intent.getLongExtra("centerId", 0L)
        fitnessCenter = FitnessCenter(id = centerId)
        userInfo = intent.getParcelableExtra<UserInfo>("userInfo")!!

        textViewItemName.text = intent.getStringExtra("centerName")
        textViewItemPrice.text = "${intent.getLongExtra("centerPrice", 0L)}원"
//        textViewRating.text = intent.getLongExtra("centerRating",0).toString()
        textViewItemAddress.text = intent.getStringExtra("centerLocation")
        textViewCenterText.text = """
            ${intent.getStringExtra("centerLocation")} 에 자리잡은 저희 헬스장은 최상의 시설과 탁월한 관리로 여러분의 만족을 최우선으로 합니다.
            일일권 ${intent.getLongExtra("centerPrice", 0L)}원의 가격으로 친절한 직원들과, 깨끗하고 넓은 시설을 체험해 보세요.
            다양한 최신 운동 기구와 넓은 공간에서 여러분만의 운동 루틴도 만들어 보세요.
        """.trimIndent()

        Glide.with(this)
            .load(intent.getStringExtra("centerImageUrl"))
            .into(imageView)

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

        callReview(centerId)
    }

    private fun addReview() {
        val reviewText = reviewEditText.text.toString()
        val rating = ratingBar.rating.toInt()
        Log.d("평점","평점은 : ${ratingBar.rating.toInt()}")
        if (reviewText.isNotEmpty()) {
            lifecycleScope.launch {
                try {
                    val centerId = intent.getLongExtra("centerId", 0L)
                    val userInfo = intent.getParcelableExtra<UserInfo>("userInfo")
                    val userId = userInfo?.Id
                    val review = Review(
                        userId = userId,
                        centerId = centerId,
                        rating = rating,
                        reviewText = reviewText
                    )

                    val response = withContext(Dispatchers.IO) {
                        RetrofitInstance.reviewService.addReview(review).enqueue(object :
                            Callback<ResponseBody> {
                            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                if (response.isSuccessful) {
                                    Toast.makeText(
                                        this@CenterDetailActivity,
                                        "리뷰가 성공적으로 등록되었습니다.",
                                        Toast.LENGTH_SHORT,

                                        ).show()
                                    reviewEditText.setText("")
                                    callReview(centerId)

                                } else {
                                    Toast.makeText(
                                        this@CenterDetailActivity,
                                        "리뷰 등록에 실패했습니다.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            }

                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                Toast.makeText(
                                    this@CenterDetailActivity,
                                    "리뷰 등록에 실패했습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.e("Add Review", "Failed to add review: ${t.message}")
                            }
                        })
                    }
                } catch (e: HttpException) {
                    Toast.makeText(this@CenterDetailActivity, "리뷰 등록에 실패했습니다.", Toast.LENGTH_SHORT)
                        .show()
                    Log.e("Add Review", "Failed to add review: ${e.message()}")
                } catch (e: Throwable) {
                    Toast.makeText(this@CenterDetailActivity, "리뷰 등록에 실패했습니다.", Toast.LENGTH_SHORT)
                        .show()
                    Log.e("Add Review", "Failed to add review: ${e.message}")
                }
            }
        } else {
            Toast.makeText(this, "리뷰를 작성해주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun callReview(centerId: Long) {
        val reviewService = RetrofitInstance.reviewService
        val textViewRating = findViewById<TextView>(R.id.centerRating)
        reviewService.getAllReviews(centerId).enqueue(object : Callback<List<Review>> {
            override fun onResponse(call: Call<List<Review>>, response: Response<List<Review>>) {
                if (response.isSuccessful) {
                    val reviews = response.body() ?: emptyList()
                    Log.d("리뷰 불러오기", "불러온 리뷰들 : $reviews")

                    if (reviews.isNotEmpty()) {
                        // 리뷰가 있는 경우에만 평균 계산
                        val ratings: List<Int> = reviews.mapNotNull { it.rating } // 리뷰에서 평점만 추출하여 리스트로 변환
                        val sum = ratings.sum() // 평점의 합계 계산
                        val averageRating = if (ratings.isNotEmpty()) sum.toDouble() / ratings.size else 0.0 // 평균 계산
                        textViewRating.text = String.format("%.1f", averageRating)
                    } else {
                        // 리뷰가 없는 경우
                        textViewRating.text = "평가 없음"
                    }

                    // 리뷰 리스트를 성공적으로 가져왔으므로 이후 작업 수행
                    updateReviewRecyclerView(reviews)
                } else {
                    Log.e("Response Error", "Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Review>>, t: Throwable) {
                Log.e("Request Failed", "Error: ${t.message}", t)
            }
        })
    }


    private fun updateReviewRecyclerView(reviews: List<Review>) {
        reviewAdapter = ReviewAdapter(reviews)
        reviewView.layoutManager = LinearLayoutManager(this)
        reviewView.adapter = reviewAdapter
    }

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

    private fun showReservationConfirmationDialog(reservationDate: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("예약 확인")
        val message = "예약을 진행하시겠습니까?\n\n예약 날짜: $reservationDate"
        builder.setMessage(message)
        builder.setPositiveButton("예약") { dialog, _ ->
            DialogHelper.showMessageDialog(this, "예약 확인", "예약이 완료되었습니다.\n\n예약 날짜: $reservationDate") {
                serverDbSaveReservation(userInfo, fitnessCenter, reservationDate)
                val intent = Intent(this, MyTicketActivity::class.java)
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

    private fun serverDbSaveReservation(userInfo: UserInfo, fitnessCenter: FitnessCenter, reservationDate: String) {
        val reservationObj = Reservation(center = fitnessCenter, user = userInfo.toUser(), reservationTime = reservationDate )

        val reservationService = RetrofitInstance.reservationService
        reservationService.createReservation(reservationObj).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("예약성공", "DB확인해보세요 있음 ㄹㅇㅋㅋ")
                } else {
                    Log.e("Reservation Error", "Failed to create reservation. Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("Reservation Error", "Failed to create reservation. Error: ${t.message}", t)
            }
        })

    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null && ev != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            currentFocus?.clearFocus()
        }
        return super.dispatchTouchEvent(ev)
    }
}