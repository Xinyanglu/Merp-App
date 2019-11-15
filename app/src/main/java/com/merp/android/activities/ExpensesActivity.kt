package com.merp.android.activities

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.merp.android.R

import kotlinx.android.synthetic.main.activity_expenses.*

class ExpensesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expenses)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            startActivity(Intent(this, EditExpenseActivity::class.java))
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
