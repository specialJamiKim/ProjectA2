package com.example.projecta2.View

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projecta2.R
import com.example.projecta2.adapter.FitnessCenterAdapter
import com.example.projecta2.model.FitnessCenter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import java.time.LocalTime

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var map: GoogleMap
    private lateinit var recyclerView: RecyclerView
    private lateinit var fitnessCenterAdapter: FitnessCenterAdapter
    private lateinit var fitnessCenters: List<FitnessCenter>

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val homeButton: LinearLayout = findViewById(R.id.mapToHome)
        val myPageButton: LinearLayout = findViewById(R.id.mapToMyPage)

        homeButton.setOnClickListener {
            startActivity(Intent(this@MapActivity, HomeActivity::class.java))
        }

        myPageButton.setOnClickListener {
            startActivity(Intent(this@MapActivity, MyPageActivity::class.java))
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapView) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        // 임시 데이터 생성 및 RecyclerView 설정
        setupRecyclerView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupRecyclerView() {
/*        fitnessCenters = listOf(
            FitnessCenter(1, "어나더짐", "부산진구", "051-111-2222", 10000, LocalTime.of(6, 0), LocalTime.of(23, 0), null),
            FitnessCenter(2, "워너짐", "부산 수영구", "051-765-4321", 20000, LocalTime.of(6, 30), LocalTime.of(22, 0), null)
            // 추가 임시 데이터
        )*/

        recyclerView = findViewById<RecyclerView>(R.id.recycler).apply {
            layoutManager = LinearLayoutManager(this@MapActivity, LinearLayoutManager.VERTICAL, false)
            adapter = FitnessCenterAdapter(fitnessCenters)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        getUserLocation()
    }

    private fun getUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1000)
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val userLocation = LatLng(location.latitude, location.longitude)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14f))
                map.addCircle(CircleOptions()
                    .center(userLocation)
                    .radius(1000.0)
                    .strokeWidth(0f)
                    .fillColor(0x5500ff00.toInt()))
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1000 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getUserLocation()
        }
    }
}
