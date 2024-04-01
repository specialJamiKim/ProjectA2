package com.example.projecta2.View

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.projecta2.R
import com.example.projecta2.adapter.BannerAdapter
import com.example.projecta2.adapter.HomeAdapter
import com.example.projecta2.model.FitnessCenter
import com.example.projecta2.util.RetrofitInstance
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private lateinit var personLinearLayout: LinearLayout
    private lateinit var homeToMap: FloatingActionButton
    private lateinit var myTicketCardView: CardView
    private lateinit var imgBannerRecyclerView: RecyclerView
    private lateinit var autoScrollHandler: Handler
    private lateinit var autoScrollRunnable: Runnable
    private var autoScrollDelay: Long = 2000 // 2초
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

    private fun initView() {
        personLinearLayout = findViewById(R.id.person_linear_layout)
        homeToMap = findViewById(R.id.homeToMap)
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

        myTicketCardView.setOnClickListener {
            startActivity(Intent(this, MyTicketActivity::class.java))
        }
    }

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

    private fun setupRecyclerView() {
        HomeRecycler = findViewById(R.id.HomeRecycler)
        HomeRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val adapter = HomeAdapter(fitnessCenters) { fitnessCenter ->
            val intent = Intent(this@HomeActivity, CenterDetailActivity::class.java).apply {
                putExtra("centerName", fitnessCenter.name)
                putExtra("centerPrice", fitnessCenter.dailyPassPrice) // Long 타입으로 전달
                putExtra("centerLocation", fitnessCenter.address)
                putExtra("centerImageUrl", fitnessCenter.imagePath?.let { "http://10.0.2.2:8111/img/$it" })
            }
            startActivity(intent)
        }
        HomeRecycler.adapter = adapter
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
