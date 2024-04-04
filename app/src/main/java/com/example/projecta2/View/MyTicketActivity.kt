package com.example.projecta2.View

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projecta2.Entity.UserInfo
import com.example.projecta2.R
import com.example.projecta2.model.Reservation
import com.example.projecta2.model.Result
import com.example.projecta2.util.RetrofitInstance

import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyTicketActivity : AppCompatActivity() {

    private lateinit var userInfo: UserInfo

    private lateinit var ticketPageUserName: TextView
    private lateinit var tvMyTicketCount : TextView
    private lateinit var ticketUseRecycler : RecyclerView
    private lateinit var todayReservationList : List<Reservation>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_ticket)

        //예약개수
        tvMyTicketCount = findViewById(R.id.tvMyTicketCount)
        ticketUseRecycler = findViewById(R.id.ticketUseRecycler)
        // 사용자 정보를 인텐트로부터 가져옵니다.
        userInfo = intent.getParcelableExtra<UserInfo>("userInfo")!!

        // 사용자 이름을 화면에 표시합니다.
        val ticketPageUserName: TextView = findViewById(R.id.ticketPageUserName)
        ticketPageUserName.text = userInfo.name

        val reservationService = RetrofitInstance.reservationService

        reservationService.getUserReservations(userInfo.Id).enqueue(object :
            Callback<Result<Reservation>> {
            override fun onResponse(
                call: Call<Result<Reservation>>,
                response: Response<Result<Reservation>>
            ) {
                Log.d("수행시작", "요청부분 수행중입니다.")
                if (response.isSuccessful) {
                    val result: Result<Reservation>? = response.body()
                    if (result != null) {
                        //당일 날짜에 예약된 것만 표시 => 헬스장 정보, 카운트 따로 보관
                        todayReservationList = result.data
                        // 어댑터 인스턴스 생성
                        val adapter = MyTicketAdapter(todayReservationList)
                        //당일예약 숫자 카운팅 세팅
                        tvMyTicketCount.text = todayReservationList.size.toString()
                        // 리사이클러뷰에 어댑터 설정
                        ticketUseRecycler.adapter = adapter
                        // 리사이클러뷰에 변경 사항을 알림
                        adapter.notifyDataSetChanged()
                    } else {
                        // 응답 바디가 null인 경우 처리
                        Log.e("Reservation Error", "Failed to get reservation list. Response body is null.")
                    }
                } else {
                    // 서버로부터 응답이 실패한 경우 처리
                    Log.e("Reservation Error", "Failed to get reservation list. Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Result<Reservation>>, t: Throwable) {
                // 요청이 실패한 경우 처리
                Log.e(
                    "Reservation Error 통신 아예실패",
                    "Failed to get reservation list. Error: ${t.message}",
                    t
                )
            }
        })


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home_linear_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            WindowInsetsCompat.CONSUMED
        }

        // 홈 버튼 클릭 리스너 설정
        val homeImageView: ImageView = findViewById(R.id.homeImageViewUserEdit4)
        homeImageView.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        // 지도 버튼 클릭 리스너 설정
        val mapFloatingActionButton: FloatingActionButton = findViewById(R.id.mapFabUserEdit4)
        mapFloatingActionButton.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }

        // 마이페이지 버튼 클릭 리스너 설정
        val myPageImageView: ImageView = findViewById(R.id.myPageImageView4)
        myPageImageView.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }

        // 리사이클러뷰 설정
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.ticketUseRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 예시 데이터 리스트 (실제 앱에서는 서버 또는 데이터베이스에서 데이터를 로드해야 합니다)
        val reservationList = listOf<Reservation>() // 여기에 데이터를 채워넣으세요.

        recyclerView.adapter = MyTicketAdapter(reservationList)
    }
}

