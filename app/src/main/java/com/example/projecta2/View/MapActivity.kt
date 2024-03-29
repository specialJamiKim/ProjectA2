package com.example.projecta2.View

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projecta2.R
import com.example.projecta2.adapter.FitnessCenterAdapter
import com.example.projecta2.api.SignIn
import com.example.projecta2.model.FitnessCenter
import com.example.projecta2.model.User
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.time.LocalTime
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var map: GoogleMap
    private lateinit var recyclerView: RecyclerView
    private lateinit var fitnessCenterAdapter: FitnessCenterAdapter
    private lateinit var fitnessCenters: List<FitnessCenter>

    private lateinit var zoomInButton: ImageButton
    private lateinit var zoomOutButton: ImageButton
    private lateinit var homeButton: LinearLayout
    private lateinit var myPageButton: LinearLayout

    private val REQUEST_PERMISSION_LOCATION = 1000

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // UI 컴포넌트 초기화
        zoomInButton = findViewById(R.id.zoomInButton)
        zoomOutButton = findViewById(R.id.zoomOutButton)
        homeButton = findViewById(R.id.mapToHome)
        myPageButton = findViewById(R.id.mapToMyPage)

        // Zoom 버튼 클릭 리스너
        zoomInButton.setOnClickListener { zoomIn() }
        zoomOutButton.setOnClickListener { zoomOut() }

        // 홈 및 마이페이지 버튼 클릭 리스너
        homeButton.setOnClickListener {
            startActivity(
                Intent(
                    this@MapActivity,
                    HomeActivity::class.java
                )
            )
        }
        myPageButton.setOnClickListener {
            startActivity(
                Intent(
                    this@MapActivity,
                    MyPageActivity::class.java
                )
            )
        }

        // 지도 및 리사이클러뷰 설정
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapView) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        setupRecyclerView()
    }

    private fun zoomIn() {
        map.animateCamera(CameraUpdateFactory.zoomIn())
    }

    private fun zoomOut() {
        map.animateCamera(CameraUpdateFactory.zoomOut())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupRecyclerView() {
        // 임시 데이터 및 RecyclerView 설정
        fitnessCenters = listOf(
            FitnessCenter(
                1,
                "어나더짐",
                "부산진구",
                "051-111-2222",
                10000,
                LocalTime.of(6, 0),
                LocalTime.of(23, 0),
                null
            ),
            FitnessCenter(
                2,
                "워너짐",
                "부산 수영구",
                "051-765-4321",
                20000,
                LocalTime.of(6, 30),
                LocalTime.of(22, 0),
                null
            )
        )

        recyclerView = findViewById(R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = FitnessCenterAdapter(fitnessCenters)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        getUserLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getUserLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_PERMISSION_LOCATION
            )
            return
        }

        // 사용자의 마지막 위치를 가져오고, 위치가 존재하는 경우 Google Map에 표시
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val userLocation = LatLng(it.latitude, it.longitude)
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14f))
                map.addMarker(MarkerOptions().position(userLocation).title("Your Location"))

                // 사용자 위치 주변에 원을 그립니다. (옵션)
                map.addCircle(
                    CircleOptions()
                        .center(userLocation)
                        .radius(1000.0) // 1km
                        .strokeWidth(0f)
                        .fillColor(0x5500ff00.toInt())
                )
            }
        }
    }
}

