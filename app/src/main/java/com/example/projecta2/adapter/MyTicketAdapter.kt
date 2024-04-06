package com.example.projecta2.View

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projecta2.R
import com.example.projecta2.model.Reservation
import com.example.projecta2.util.DialogHelper.showConfirmationDialog
import com.example.projecta2.util.RetrofitInstance
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyTicketAdapter(private var reservationList: MutableList<Reservation>, private val onDeleteListener: OnDeleteListener) :
    RecyclerView.Adapter<MyTicketAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btnTicketUsed: Button = itemView.findViewById(R.id.btnTicketUsed)
        val btnReservationCancel : Button = itemView.findViewById(R.id.btnReservationCancel)
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

        val reservationService = RetrofitInstance.reservationService

        holder.btnTicketUsed.setOnClickListener {
            reservationService.reservationUsed(reservation.id).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        reservationList.remove(reservation)
                        notifyDataSetChanged()
                        onDeleteListener.onDelete()
                    } else {
                        Log.e("Reservation Error", "Failed to mark reservation as used. Code: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("Reservation Error", "Error: ${t.message}")
                }
            })
        }

        holder.btnReservationCancel.setOnClickListener{
            showConfirmationDialog(context = holder.itemView.context,
                title = "예약 취소",
                message = "정말로 예약을 취소하시겠습니까?",
                onPositiveClick = {
                    reservationService.reservationCancel(reservation.id).enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            if (response.isSuccessful) {
                                reservationList.remove(reservation)
                                notifyDataSetChanged()
                                onDeleteListener.onDelete()
                            } else {
                                Log.e("Reservation Error", "Failed to cancel reservation. Code: ${response.code()}")
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Log.e("Reservation Error", "Error: ${t.message}")
                        }
                    })
                }
            )
        }

    }

    override fun getItemCount() = reservationList.size

// 새로운 예약 리스트로 어댑터를 업데이트하는 메서드
fun updateList(newReservationList: MutableList<Reservation>) {
    reservationList.clear()
    reservationList.addAll(newReservationList)
    notifyDataSetChanged()
}



    interface OnDeleteListener {
        fun onDelete()
    }
}
