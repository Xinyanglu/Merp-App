package com.merp.android.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.merp.android.R
import kotlinx.android.synthetic.main.activity_view_reports.*
import kotlinx.android.synthetic.main.content_view_reports.*

class ViewReportsActivity : AppCompatActivity() {

    private fun setup(){
        ShowEarnings.setOnClickListener{
            val intent = Intent(this, ViewReportsActivity::class.java)
            intent.putExtra("entry","earnings")
            intent.putExtra("method","bar")
            startActivity(intent)
            finish()
        }

        ShowExpenses.setOnClickListener{
            val intent = Intent(this, ViewReportsActivity::class.java)
            intent.putExtra("entry","expenses")
            intent.putExtra("method", "bar")
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_reports)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val intent = this.intent
        val extras = Bundle()
        extras.putString("entry", intent.getStringExtra("entry"))
        extras.putString("method", intent.getStringExtra("method"))
        val chartFragment = ChartFragment()
        chartFragment.arguments = extras

        supportFragmentManager.beginTransaction().add(R.id.fragment_container,chartFragment).commit()

        setup()
    }
}
