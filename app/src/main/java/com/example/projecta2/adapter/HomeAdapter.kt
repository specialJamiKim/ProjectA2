package com.example.projecta2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projecta2.R
import com.example.projecta2.databinding.HomeFitnessListItemBinding
import com.example.projecta2.model.FitnessCenter

class HomeAdapter(
    private val fitnessCenterList: List<FitnessCenter>, // 수정됨: 타입을 List<FitnessCenter>로 변경
    private val onItemClicked: (FitnessCenter) -> Unit
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HomeFitnessListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fitnessCenter = fitnessCenterList[position]
        with(holder) {
            binding.apply {
                // 이미지 로드 및 표시
                Glide.with(homePageCenterImg.context)
                    .load(fitnessCenter.imagePath?.let { "http://10.0.2.2:8111/img/$it" } ?: R.drawable.favorite_img_7)
                    .placeholder(R.drawable.chair_white_bg) // 로딩 중에 표시할 이미지
                    .error(R.drawable.chair_light_orange_bg) // 에러 발생 시 표시할 이미지
                    .into(homePageCenterImg)

                // 나머지 데이터 표시
                textViewItemName.text = fitnessCenter.name
                textViewItemPrice.text = "${fitnessCenter.dailyPassPrice}원"

                root.setOnClickListener {
                    onItemClicked(fitnessCenter)
                }
            }
        }
    }

    override fun getItemCount(): Int = fitnessCenterList.size

    class ViewHolder(val binding: HomeFitnessListItemBinding) : RecyclerView.ViewHolder(binding.root)
}
