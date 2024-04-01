package com.example.projecta2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projecta2.R
import com.example.projecta2.databinding.HomeFitnessListItemBinding
import com.example.projecta2.model.FitnessCenter


class HomeAdapter(private val fitnessCenterList: List<FitnessCenter>) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HomeFitnessListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val fitnessCenter = fitnessCenterList[position]

            // 이미지 로드 및 표시
            if (fitnessCenter.imagePath != null && fitnessCenter.imagePath.isNotEmpty()) {
                val imageUrl = "http://10.0.2.2:8111/img/${fitnessCenter.imagePath}"
                Glide.with(binding.homePageCenterImg.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.chair_white_bg) // 로딩 중에 표시할 이미지
                    .error(R.drawable.chair_light_orange_bg) // 에러 발생 시 표시할 이미지
                    .into(binding.homePageCenterImg)
            } else {
                // 이미지가 없을 경우에 대한 처리
                binding.homePageCenterImg.setImageResource(R.drawable.favorite_img_7)
            }

            // 나머지 데이터 표시
            binding.textViewItemName.text = fitnessCenter.name
            binding.textViewItemPrice.text = "${fitnessCenter.dailyPassPrice}원"
        }
    }


    override fun getItemCount(): Int = fitnessCenterList.size

    class ViewHolder(val binding: HomeFitnessListItemBinding) : RecyclerView.ViewHolder(binding.root)
}
