package com.merp.android.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.merp.android.R
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainDate.text = getDate().format(Date())
        btnEarnings.setOnClickListener {
            startActivity(Intent(this, TempViewExpenses::class.java))
        }

        btnExpenses.setOnClickListener {
            TODO()
        }

        btnReports.setOnClickListener {
            TODO()
        }
    }

    override fun onResume() {
        super.onResume()
        mainDate.text = getDate().format(Date())
    }

    //Gets current date in format: October 15, 2019
    private fun getDate(): SimpleDateFormat = SimpleDateFormat("yyyy, MMMM dd", Locale.CANADA)
}