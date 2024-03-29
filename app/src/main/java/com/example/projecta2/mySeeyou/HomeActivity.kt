package com.example.projecta2.View

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.projecta2.R
import com.example.projecta2.adapter.BannerAdapter
import com.example.projecta2.api.UserSelector
import com.example.projecta2.model.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeActivity : AppCompatActivity() {

    private lateinit var personLinearLayout: LinearLayout
    private lateinit var homeToMap: FloatingActionButton
    private lateinit var cactusCardView: CardView
    private lateinit var myTicketCardView: CardView
    private lateinit var wannaGymCardView: CardView
    private lateinit var imgBannerRecyclerView: RecyclerView
    private lateinit var autoScrollHandler: Handler
    private lateinit var autoScrollRunnable: Runnable
    private lateinit var btnGo: Button // 삭제
    private var autoScrollDelay: Long = 2000 // 2초

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initView()
        setupListeners()
        setupBannerRecyclerView()

        btnGo = findViewById(R.id.btnGo)

        btnGo.setOnClickListener {
            getUserInfo("12")
        }

    }

    private fun initView() {
        personLinearLayout = findViewById(R.id.person_linear_layout)
        homeToMap = findViewById(R.id.homeToMap)
        cactusCardView = findViewById(R.id.cactus_card_view)
        wannaGymCardView = findViewById(R.id.wannaGym_card_view)
        myTicketCardView = findViewById(R.id.myTicketCardView)
        imgBannerRecyclerView = findViewById(R.id.imgBannerRecyclerView)
    }

    private fun setupListeners() {

        personLinearLayout.setOnClickListener {
            startActivity(Intent(this, MyPageActivity::class.java))
        }

        homeToMap.setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
        }

        cactusCardView.setOnClickListener {
            startActivity(Intent(this, CenterDetailActivity::class.java).apply {
                putExtra("itemName1", "비나이더짐")
                putExtra("itemPrice1", "15,000원")
            })
        }

        wannaGymCardView.setOnClickListener {
            startActivity(Intent(this, CenterDetailActivity::class.java).apply {
                putExtra("itemName2", "워너짐 수영구점")
                putExtra("itemPrice2", "10,000원")
            })
        }

        myTicketCardView.setOnClickListener {
            startActivity(Intent(this, MyTicketActivity::class.java))
        }
    }

    private fun setupBannerRecyclerView() {
        val images = listOf(R.drawable.bannerimg, R.drawable.bannerimg2, R.drawable.bannerimg3)
        imgBannerRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        imgBannerRecyclerView.adapter = BannerAdapter(images)

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(imgBannerRecyclerView)
    }

    override fun onResume() {
        super.onResume()
        startAutoScrollBanner()
    }

    override fun onPause() {
        stopAutoScrollBanner()
        super.onPause()
    }

    private fun getUserInfo(email: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8111/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(UserSelector::class.java)

        service.userSelect(email).enqueue(object : Callback<User> {

            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    val userName = user?.name
                    Log.d("표시", "${userName}")
                    // 마이페이지로 이동하면서 사용자 이름을 전달합니다.
                    val intent = Intent(this@HomeActivity, MyPageActivity::class.java).apply {
                        putExtra("userName", userName)
                        Log.d(">>", "굿")
                    }
                    startActivity(intent)
                } else {
                    // 서버로부터 응답을 받지 못한 경우 처리
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                // API 호출 실패 시 처리
            }
        })
    }


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

    private fun stopAutoScrollBanner() {
        autoScrollHandler.removeCallbacks(autoScrollRunnable)
    }

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


