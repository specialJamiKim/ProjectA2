package com.example.projecta2.View

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projecta2.R
import com.example.projecta2.model.Reservation

class MyTicketAdapter(private val reservationList: List<Reservation>) :
    RecyclerView.Adapter<MyTicketAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvReservationTime: TextView = itemView.findViewById(R.id.tvReservationTime)
        val tvCenterName: TextView = itemView.findViewById(R.id.tvCenterName)
        val tvAddress: TextView = itemView.findViewById(R.id.tvAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.my_ticket_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val reservation = reservationList[position]
        holder.tvReservationTime.text = reservation.reservationTime
        holder.tvCenterName.text = reservation.center.name
        holder.tvAddress.text = reservation.center.address
    }

    override fun getItemCount() = reservationList.size
}
