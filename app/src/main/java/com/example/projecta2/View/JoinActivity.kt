package com.example.projecta2.View

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.projecta2.R
import java.util.Calendar

class JoinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        val etBirthDate = findViewById<EditText>(R.id.etBirthDate)
        etBirthDate.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, { _, year, monthOfYear, dayOfMonth ->
            // 사용자가 선택한 날짜를 EditText에 설정
            val dateStr = "${year}-${monthOfYear + 1}-${dayOfMonth}"
            findViewById<EditText>(R.id.etBirthDate).setText(dateStr)
        }, year, month, day)

        dpd.show()
    }
}