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
import com.example.projecta2.util.RetrofitInstance
import okhttp3.ResponseBody
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Call

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

        val reservationService = RetrofitInstance.reservationService

        // 버튼 클릭 이벤트 처리
        holder.btnTicketUsed.setOnClickListener {
            val reservationId = reservation.id
            Log.d("예약번호 ", "$reservationId")

            // 예약 사용처리
            reservationService.reservationUsed(reservationId).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        // 성공적으로 예약이 사용되었을 때의 처리
                        Log.d("성공이다", "아싸!!")
                        // 여기서 다른 필요한 작업을 수행할 수 있습니다.
                        // 사용된 예약을 리스트에서 제거
                        val newList = reservationList.toMutableList()
                        newList.remove(reservation)
                        // 새로운 리스트로 갱신
//                        reservationList.clear()
//                        reservationList.addAll(newList)
                        notifyDataSetChanged() // 리사이클러뷰 갱신
                    } else {
                        // 서버로부터 응답이 실패한 경우 처리
                        println("Failed to mark reservation as used. Code: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // 요청이 실패한 경우 처리
                    println("이거임 Error: ${t.message}")
                }
            })
        }
    }

/*    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val reservation = reservationList[position]

        holder.tvCenterName.text = reservation.center.name
        holder.tvAddress.text = reservation.center.address

        val reservationService = RetrofitInstance.reservationService

// 버튼 클릭 이벤트 처리
        holder.btnTicketUsed.setOnClickListener {
            val reservationId = reservation.id
            Log.d("예약번호 " , "${reservationId}")

            // 예약 사용처리
            reservationService.reservationUsed(reservationId).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        // 성공적으로 예약이 사용되었을 때의 처리
                            Log.d("성공이다","아싸!!")
                        // 여기서 다른 필요한 작업을 수행할 수 있습니다.
                    } else {
                        // 서버로부터 응답이 실패한 경우 처리
                        println("Failed to mark reservation as used. Code: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // 요청이 실패한 경우 처리
                    println("이거임 Error: ${t.message}")
                }
            })
        }


    }*/

    override fun getItemCount() = reservationList.size
}
