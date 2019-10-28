package com.merp.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    }

    override fun onResume() {
        super.onResume()
        mainDate.text = getDate().format(Date())
    }

    //Gets current date in format: October 15, 2019
    fun getDate(): SimpleDateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.CANADA)
}