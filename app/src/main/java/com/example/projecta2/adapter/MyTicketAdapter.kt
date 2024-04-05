package com.example.projecta2.View

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projecta2.R
import com.example.projecta2.model.Reservation

class MyTicketAdapter(private val reservationList: List<Reservation>) :
    RecyclerView.Adapter<MyTicketAdapter.MyViewHolder>() {

    // ViewHolder 클래스
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btnTicketUsed: Button = itemView.findViewById(R.id.btnTicketUsed)
        val tvCenterName: TextView = itemView.findViewById(R.id.tvCenterName)
        val tvAddress: TextView = itemView.findViewById(R.id.tvAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.my_ticket_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val reservation = reservationList[position]

        holder.tvCenterName.text = reservation.center.name
        holder.tvAddress.text = reservation.center.address

        // 버튼 클릭 이벤트 처리
        holder.btnTicketUsed.setOnClickListener {
            val context = it.context
            val intent = Intent(context, MyTicketActivity::class.java).apply {
                putExtra("reservationId", reservation.id) // 예약 ID 전달
                // 기타 필요한 데이터 추가 전달
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = reservationList.size
}
