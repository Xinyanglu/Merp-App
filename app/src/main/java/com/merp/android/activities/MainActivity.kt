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

        setDate()

        btnEarnings.setOnClickListener {
            startActivity(Intent(this, EarningsActivity::class.java))
        }

        btnExpenses.setOnClickListener {
            startActivity(Intent(this, ExpensesActivity::class.java))
        }

        btnReports.setOnClickListener {
            TODO()
        }
    }

    override fun onResume() {
        super.onResume()
        setDate()
    }

    private fun setDate(){
        val sdf = SimpleDateFormat("yyyy, MMMM dd", Locale.getDefault())
        mainDate.text = sdf.format(Date())
    }
}