package com.example.projecta2.View

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.projecta2.R
import com.example.projecta2.model.FitnessCenter
import com.example.projecta2.model.Reservation
import com.example.projecta2.model.User
import com.example.projecta2.util.RetrofitInstance
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class CenterDetailActivity : AppCompatActivity() {
    private var centerName: String? = null
    private var imageUrl: String? = null
    private var centerId: Long = 0 // 예약에 필요한 센터 아이디

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_center_detail)

        // 액션바 숨기기
        supportActionBar?.hide()

        // UI 컴포넌트 초기화
        val textViewItemName = findViewById<TextView>(R.id.centerName)
        val textViewItemPrice = findViewById<TextView>(R.id.centerPrice)
        val textViewItemAddress = findViewById<TextView>(R.id.centeraddress)
        val textViewCenterText = findViewById<TextView>(R.id.centerText)
        val imageView = findViewById<ImageView>(R.id.centerImg)
        val backButton = findViewById<Button>(R.id.buttonBack)
        val reserveButton = findViewById<Button>(R.id.centerReservBtn)

        // Intent에서 데이터 추출
        centerName = intent.getStringExtra("itemName1")
        val itemPrice = intent.getStringExtra("itemPrice1")
        val itemAddress = intent.getStringExtra("itemAddress1")
        imageUrl = intent.getStringExtra("itemImageUrl")
        centerId = intent.getLongExtra("centerId", 0) // 센터 아이디 받아오기

        // 데이터 설정
        textViewItemName.text = centerName
        textViewItemPrice.text = itemPrice
        textViewItemAddress.text = itemAddress
        textViewCenterText.text =
            "$itemAddress 에 자리잡은 저희 헬스장은 최상의 시설과 탁월한 관리로 여러분의 만족을 최우선으로 합니다.\n일일권 $itemPrice 원의 가격으로 친절한 직원들과, 깨끗하고 넓은 시설을 체험해 보세요.\n다양한 최신 운동 기구와 넓은 공간에서 여러분만의 운동 루틴도 만들어 보세요."

        // 클릭 리스너 설정
        backButton.setOnClickListener { finish() }
        reserveButton.setOnClickListener { showReservationDialog() }

        // 이미지 로딩
        imageUrl?.let {
            if (it.isNotEmpty()) {
                Glide.with(this)
                    .load(it)
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.bannerimg)
                            .error(R.drawable.bannerimg2)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                    )
                    .into(imageView)
            } else {
                imageView.setImageResource(R.drawable.favorite_img_7) // 기본 이미지 설정
            }
        }
    }

    private fun showReservationDialog() {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                createReservation(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

    private fun createReservation(selectedDate: String) {
        val calendar = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val reservationService = RetrofitInstance.reservationService

        // 예약 객체 생성 및 센터 ID, 사용자 ID 설정
        val reservation = Reservation(
            0,
            center = FitnessCenter(1),
            user = User(1),
            reservationTime = "11"
        )

        // 서버로 예약 생성 요청 전송
        reservationService.createReservation(reservation).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    showToast("예약이 완료되었습니다.")
                } else {
                    showToast("예약에 실패했습니다.")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                showToast("네트워크 오류가 발생했습니다.")
            }
        })
    }


    private fun getLoggedInUserId(): Long {
        // 여기에서 로그인된 사용자의 ID를 가져오는 로직을 구현
        // 예시로 -1을 반환
        return -1L
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
