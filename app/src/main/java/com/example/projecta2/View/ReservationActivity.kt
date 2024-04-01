package com.example.projecta2.View

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.projecta2.R
import java.util.*

class ReservationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation)

        // 홈 버튼 클릭 시 이벤트 처리

        // 예약하기 버튼 클릭 시 이벤트 처리
        val btnReserve = findViewById<Button>(R.id.btnReserve)
        btnReserve.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val reservationDate = "$year-${monthOfYear + 1}-$dayOfMonth"
                showTimePicker(reservationDate)
            }, year, month, day)

        datePickerDialog.show()
    }

    private fun showTimePicker(reservationDate: String) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this,
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                val reservationTime = "$hourOfDay:$minute"
                showReservationConfirmationDialog(reservationDate, reservationTime)
            }, hour, minute, false)

        timePickerDialog.show()
    }

    private fun showReservationConfirmationDialog(reservationDate: String, reservationTime: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("예약 확인")
        val message = "예약을 진행하시겠습니까?\n\n예약 날짜: $reservationDate\n예약 시간: $reservationTime"
        builder.setMessage(message)

        val input = EditText(this)
        input.hint = "이름을 입력하세요"
        builder.setView(input)

        builder.setPositiveButton("예약") { dialog, which ->
            val userName = input.text.toString().trim()
            if (userName.isNotEmpty()) {
                // 여기에 예약 로직을 구현합니다.
                val confirmationMessage = "예약이 완료되었습니다.\n\n예약 날짜: $reservationDate\n예약 시간: $reservationTime\n예약자 이름: $userName"
                Toast.makeText(this, confirmationMessage, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "이름을 입력하세요", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("취소") { dialog, which ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }
}
