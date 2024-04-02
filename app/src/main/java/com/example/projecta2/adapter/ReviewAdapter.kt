package com.example.projecta2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projecta2.R
import com.example.projecta2.model.Review

class ReviewAdapter(private var reviewList: List<Review>) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    // 데이터를 업데이트하는 메소드 추가
    fun updateData(newData: List<Review>) {
        reviewList = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviewList[position]
        holder.reviewText.text = review.reviewText
    }

    override fun getItemCount(): Int = reviewList.size

    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reviewText: TextView = itemView.findViewById(R.id.reviewText)
    }
}
