package com.example.projecta2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projecta2.R
import com.example.projecta2.model.Review

class ReviewAdapter(private var reviews: List<Review>) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val authorTextView: TextView = itemView.findViewById(R.id.reviewAuthor)
        private val ratingTextView: TextView = itemView.findViewById(R.id.reviewRating)
        private val contentTextView: TextView = itemView.findViewById(R.id.reviewContent)

        fun bind(review: Review) {
            // 작성자 ID 설정
            authorTextView.text = review.userId.toString()

            // 평점 설정 (사용되지 않았을 때는 생략 가능)
            ratingTextView.text = review.rating.toString()

            // 리뷰 내용 설정
            contentTextView.text = review.reviewText
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.reviewlist_item, parent, false)
        return ReviewViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    fun setReviews(reviews: List<Review>) {
        this.reviews = reviews
        notifyDataSetChanged()
    }
}