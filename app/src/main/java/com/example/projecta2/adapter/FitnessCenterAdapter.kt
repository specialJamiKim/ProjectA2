package com.example.projecta2.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projecta2.R
import com.example.projecta2.View.CenterDetailActivity
import com.example.projecta2.databinding.FitnessCenterItemBinding
import com.example.projecta2.model.FitnessCenter

class FitnessCenterAdapter(private val fitnessCenterList: List<FitnessCenter>) :
    RecyclerView.Adapter<FitnessCenterAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FitnessCenterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fitnessCenter = fitnessCenterList[position]
        with(holder) {
            // 이미지 로드 및 표시
            fitnessCenter.imagePath?.let {
                if (it.isNotEmpty()) {
                    val imageUrl = "http://10.0.2.2:8111/img/$it"
                    Glide.with(binding.ivFitnessCenterImage.context)
                        .load(imageUrl)
                        .placeholder(R.drawable.chair_white_bg) // 로딩 중에 표시할 이미지
                        .error(R.drawable.chair_light_orange_bg) // 에러 발생 시 표시할 이미지
                        .into(binding.ivFitnessCenterImage)
                } else {
                    // 이미지가 없을 경우에 대한 처리
                    binding.ivFitnessCenterImage.setImageResource(R.drawable.favorite_img_7)
                }
            }

            // 나머지 데이터 표시
            binding.tvFitnessCenterName.text = fitnessCenter.name
            binding.tvFitnessCenterDailyPassPrice.text = "${fitnessCenter.dailyPassPrice}원"
            binding.tvFitnessCenterdistance.text = "${fitnessCenter.distance} m"
            binding.tvFitnessCenterAddress.text = fitnessCenter.address

            // 클릭 리스너 설정
            itemView.setOnClickListener {
                val intent = Intent(it.context, CenterDetailActivity::class.java).apply {
                    putExtra("itemName1", fitnessCenter.name)
                    putExtra("itemPrice1", fitnessCenter.dailyPassPrice.toString())
                    putExtra("itemAddress1", fitnessCenter.address)
                    putExtra("itemImageUrl", "http://10.0.2.2:8111/img/${fitnessCenter.imagePath}") // 이미지 URL 추가
                }
                it.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = fitnessCenterList.size

    class ViewHolder(val binding: FitnessCenterItemBinding) : RecyclerView.ViewHolder(binding.root)
}
