package com.example.projecta2.View

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.projecta2.R
import java.util.*

class ReservationActivity : AppCompatActivity() {

    private var selectedDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation)

        // 예약하기 버튼 클릭 시 이벤트 처리
        val btnReserve = findViewById<Button>(R.id.btnReserve)
        btnReserve.setOnClickListener {
            showTimePicker()
        }
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                val reservationTime = "$hourOfDay:$minute"
                showReservationConfirmationDialog(reservationTime)
            }, hour, minute, false)

        timePickerDialog.show()
    }

    private fun showReservationConfirmationDialog(reservationTime: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("예약 확인")
        val message = "예약을 진행하시겠습니까?\n\n예약 시간: $reservationTime"
        builder.setMessage(message)

        builder.setPositiveButton("예약") { dialog, _ ->
            val userName = getLoggedInUserName()
            if (selectedDate != null && userName.isNotEmpty()) {
                // 여기에 예약 로직을 구현합니다.
                val confirmationMessage = "예약이 완료되었습니다.\n\n예약 날짜: $selectedDate\n예약 시간: $reservationTime\n예약자 이름: $userName"
                showToast(confirmationMessage)
            } else {
                showToast("날짜를 선택하세요")
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("취소") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun getLoggedInUserName(): String {
        // 여기에서 로그인된 사용자의 이름을 가져오는 로직을 구현
        // 예시로 "John Doe"를 반환하도록 함
        return "John Doe"
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
