package com.example.projecta2.View

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.projecta2.Entity.UserInfo
import com.example.projecta2.R
import com.example.projecta2.adapter.BannerAdapter
import com.example.projecta2.adapter.HomeAdapter
import com.example.projecta2.model.FitnessCenter
import com.example.projecta2.util.RetrofitInstance
import com.example.projecta2.util.getUserObject
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    // 뷰 변수 선언
    private lateinit var personLinearLayout: LinearLayout
    private lateinit var homeToMap: FloatingActionButton
    private lateinit var myTicketCardView: CardView
    private lateinit var imgBannerRecyclerView: RecyclerView
    private lateinit var autoScrollHandler: Handler
    private lateinit var autoScrollRunnable: Runnable
    private var autoScrollDelay: Long = 2000 // 배너 자동 스크롤 지연 시간 (2초)
    private lateinit var HomeRecycler: RecyclerView
    private lateinit var fitnessCenters: List<FitnessCenter>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initView()
        setupBannerRecyclerView()
        checkCenter()
        setupListeners()
    }

    // 뷰 초기화 메서드
    private fun initView() {
        personLinearLayout = findViewById(R.id.person_linear_layout)
        homeToMap = findViewById(R.id.homeToMap)
        myTicketCardView = findViewById(R.id.myTicketCardView)
        imgBannerRecyclerView = findViewById(R.id.imgBannerRecyclerView)
    }

    // 리스너 설정 메서드
    private fun setupListeners() {
        lifecycleScope.launch {
            val userInfo: UserInfo? = getUserObject(this@HomeActivity).getUserInfo()

            // 내 보유 일일권 카드뷰 클릭 이벤트 리스너
            myTicketCardView.setOnClickListener {
                val intent = Intent(this@HomeActivity, MyTicketActivity::class.java).apply {
                    putExtra("userInfo", userInfo)
                }
                startActivity(intent)
            }
        }

        // 지도 보기 버튼 클릭 이벤트 리스너
        homeToMap.setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
        }

        // 마이페이지로 이동하는 ImageView 리스너
        findViewById<ImageView>(R.id.HomeToMyPage).setOnClickListener {
            startActivity(Intent(this, MyPageActivity::class.java)) // MyPageActivity는 마이페이지 액티비티의 실제 클래스명으로 변경해주세요.
        }
    }

    // 피트니스 센터 확인 메서드
    private fun checkCenter() {
        val gymService = RetrofitInstance.gymService

        gymService.getGymList().enqueue(object : Callback<List<FitnessCenter>> {
            override fun onResponse(call: Call<List<FitnessCenter>>, response: Response<List<FitnessCenter>>) {
                if (response.isSuccessful) {
                    fitnessCenters = response.body() ?: emptyList()
                    setupRecyclerView()
                } else {
                    Log.e("Response Error", "Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<FitnessCenter>>, t: Throwable) {
                Log.e("Request Failed", "Error: ${t.message}", t)
            }
        })
    }

    // 리사이클러뷰 설정 메서드
    private fun setupRecyclerView() {
        HomeRecycler = findViewById(R.id.HomeRecycler)
        HomeRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        lifecycleScope.launch {
            val userInfo: UserInfo? = getUserObject(this@HomeActivity).getUserInfo()
            val adapter = HomeAdapter(fitnessCenters) { fitnessCenter ->
                val intent = Intent(this@HomeActivity, CenterDetailActivity::class.java).apply {
                    // 센터 상세 정보에 대한 인텐트 설정
                    putExtra("centerId", fitnessCenter.id)
                    putExtra("centerName", fitnessCenter.name)
                    putExtra("centerPrice", fitnessCenter.dailyPassPrice)
                    putExtra("centerLocation", fitnessCenter.address)
                    putExtra("centerImageUrl", fitnessCenter.imagePath?.let { "http://10.0.2.2:8111/img/$it" })
                    putExtra("userInfo", userInfo)
                }
                startActivity(intent)
            }
            HomeRecycler.adapter = adapter
        }
    }

    // 배너 리사이클러뷰 설정 메서드
    private fun setupBannerRecyclerView() {
        val images = listOf(R.drawable.bannerimg, R.drawable.bannerimg2, R.drawable.bannerimg3)
        imgBannerRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        imgBannerRecyclerView.adapter = BannerAdapter(images)

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(imgBannerRecyclerView)
    }

    // 액티비티 재개시 배너 자동 스크롤 시작
    override fun onResume() {
        super.onResume()
        startAutoScrollBanner()
    }

    // 액티비티 일시정지시 배너 자동 스크롤 중지
    override fun onPause() {
        stopAutoScrollBanner()
        super.onPause()
    }

    // 배너 자동 스크롤 시작 메서드
    private fun startAutoScrollBanner() {
        autoScrollHandler = Handler()
        autoScrollRunnable = object : Runnable {
            override fun run() {
                var scrollPosition = (imgBannerRecyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                scrollPosition = (scrollPosition + 1) % (imgBannerRecyclerView.adapter?.itemCount ?: 1)
                imgBannerRecyclerView.smoothScrollToPosition(scrollPosition)
                autoScrollHandler.postDelayed(this, autoScrollDelay)
            }
        }
        autoScrollHandler.postDelayed(autoScrollRunnable, autoScrollDelay)
    }

    // 배너 자동 스크롤 중지 메서드
    private fun stopAutoScrollBanner() {
        autoScrollHandler.removeCallbacks(autoScrollRunnable)
    }

    // 화면 터치 이벤트 처리 메서드
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            currentFocus?.let { v ->
                if (v is android.widget.EditText) {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                    v.clearFocus()
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }
}
