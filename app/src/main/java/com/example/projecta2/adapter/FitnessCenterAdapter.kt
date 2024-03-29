package com.example.projecta2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projecta2.databinding.FitnessCenterItemBinding
import com.example.projecta2.model.FitnessCenter

class FitnessCenterAdapter(private val fitnessCenterList: List<FitnessCenter>) :
    RecyclerView.Adapter<FitnessCenterAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FitnessCenterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val fitnessCenter = fitnessCenterList[position]
            binding.tvFitnessCenterName.text = fitnessCenter.name
    /*        binding.tvFitnessCenterPrice.text = "${fitnessCenter.dailyPassPrice}원"
            binding.tvFitnessCenterPhoneNumber.text = fitnessCenter.phoneNumber
            binding.tvFitnessCenterAddress.text = fitnessCenter.address*/
        }
    }

    override fun getItemCount(): Int = fitnessCenterList.size

    class ViewHolder(val binding: FitnessCenterItemBinding) : RecyclerView.ViewHolder(binding.root)
}
