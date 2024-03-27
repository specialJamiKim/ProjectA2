package com.example.projecta2.View

/*
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.projecta2.R
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private var currentMarker: Marker? = null
    private lateinit var mapView: MapView // lateinit 키워드로 초기화를 나중에 할 것을 알립니다.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mapView = findViewById(R.id.mapView) // mapView 초기화
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        // ...
    }

    override fun onStart() {
        super.onStart()
        if (::mapView.isInitialized) { // mapView가 초기화되었는지 확인합니다.
            mapView.onStart()
        }
    }

    override fun onStop() {
        super.onStop()
        if (::mapView.isInitialized) {
            mapView.onStop()
        }
    }

    override fun onResume() {
        super.onResume()
        if (::mapView.isInitialized) {
            mapView.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (::mapView.isInitialized) {
            mapView.onPause()
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        if (::mapView.isInitialized) {
            mapView.onLowMemory()
        }
    }

    override fun onDestroy() {
        if (::mapView.isInitialized) {
            mapView.onDestroy()
        }
        super.onDestroy()
    }
}
*/

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.projecta2.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // 홈 버튼에 대한 참조를 가져옵니다.
        val homeButton: LinearLayout = findViewById(R.id.home_linear_layout2)
        // 마이 페이지(사람) 버튼에 대한 참조를 가져옵니다.
        val myPageButton: LinearLayout = findViewById(R.id.person_linear_layout2)

        // 홈 버튼 클릭 이벤트 리스너 설정
        homeButton.setOnClickListener {
            // HomeActivity로 이동
            val intent = Intent(this@MapActivity, HomeActivity::class.java)
            startActivity(intent)
        }

        // 마이 페이지 버튼 클릭 이벤트 리스너 설정
        myPageButton.setOnClickListener {
            // MyPageActivity로 이동
            val intent = Intent(this@MapActivity, MyPageActivity::class.java)
            startActivity(intent)
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapView) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        getUserLocation()
    }

    private fun getUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 위치 권한 요청
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1000)
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val userLocation = LatLng(location.latitude, location.longitude)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14f))

                val circleOptions = CircleOptions()
                    .center(userLocation)
                    .radius(1000.0)
                    .strokeWidth(0f)
                    .fillColor(0x5500ff00.toInt()) // 반투명한 녹색
                map.addCircle(circleOptions)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1000) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 부여된 후에 위치 가져오기를 다시 시도
                getUserLocation()
            }
        }
    }
}
