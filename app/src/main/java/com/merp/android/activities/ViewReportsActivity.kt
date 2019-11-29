package com.merp.android.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.merp.android.R
import kotlinx.android.synthetic.main.activity_view_reports.*

class ViewReportsActivity : AppCompatActivity() {

    private fun setup(){
        ShowEarnings.setOnClickListener{
            val intent = Intent(this, ChartActivity::class.java)
            intent.putExtra("entry","earnings")
            intent.putExtra("method","pie")
            startActivity(intent)
        }

        ShowExpenses.setOnClickListener{
            val intent = Intent(this, ChartActivity::class.java)
            intent.putExtra("entry","expenses")
            intent.putExtra("method", "pie")
            startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_reports)
        setup()
    }
}
