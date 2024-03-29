package com.example.projecta2.View

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.projecta2.R
import com.example.projecta2.adapter.BannerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {

    private lateinit var homeToMap: FloatingActionButton
    private lateinit var cactusCardView: CardView
    private lateinit var myTicketCardView: CardView
    private lateinit var wannaGymCardView: CardView
    private lateinit var imgBannerRecyclerView: RecyclerView
    private var autoScrollHandler: Handler? = null
    private lateinit var autoScrollRunnable: Runnable
    private var autoScrollDelay: Long = 2000 // 2 seconds

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        initView(view)
        setupListeners()
        setupBannerRecyclerView()
        return view
    }

    private fun initView(view: View) {
        homeToMap = view.findViewById(R.id.homeToMap)
        cactusCardView = view.findViewById(R.id.cactus_card_view)
        wannaGymCardView = view.findViewById(R.id.wannaGym_card_view)
        myTicketCardView = view.findViewById(R.id.myTicketCardView)
        imgBannerRecyclerView = view.findViewById(R.id.imgBannerRecyclerView)
    }

    private fun setupListeners() {
        homeToMap.setOnClickListener {
            it.context.startActivity(Intent(it.context, MapActivity::class.java))
        }

        // Similar actions for other buttons...

        cactusCardView.setOnClickListener {
            // Implement onClick
        }

        wannaGymCardView.setOnClickListener {
            // Implement onClick
        }

        myTicketCardView.setOnClickListener {
            // Implement onClick
        }
    }

    private fun setupBannerRecyclerView() {
        val images = listOf(R.drawable.bannerimg, R.drawable.bannerimg2, R.drawable.bannerimg3)
        imgBannerRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        imgBannerRecyclerView.adapter = BannerAdapter(images)

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(imgBannerRecyclerView)

        startAutoScrollBanner()
    }

    private fun startAutoScrollBanner() {
        autoScrollHandler = Handler()
        autoScrollRunnable = Runnable {
            var scrollPosition = (imgBannerRecyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            scrollPosition = (scrollPosition + 1) % (imgBannerRecyclerView.adapter?.itemCount ?: 1)
            imgBannerRecyclerView.smoothScrollToPosition(scrollPosition)
            autoScrollHandler?.postDelayed(autoScrollRunnable, autoScrollDelay)
        }
        autoScrollHandler?.postDelayed(autoScrollRunnable, autoScrollDelay)
    }

    override fun onResume() {
        super.onResume()
        startAutoScrollBanner()
    }

    override fun onPause() {
        stopAutoScrollBanner()
        super.onPause()
    }

    private fun stopAutoScrollBanner() {
        autoScrollHandler?.removeCallbacks(autoScrollRunnable)
    }
}
