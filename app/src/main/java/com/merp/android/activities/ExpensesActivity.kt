package com.merp.android.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.merp.android.Database
import com.merp.android.R

import kotlinx.android.synthetic.main.activity_expenses.*

class ExpensesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expenses)
        setSupportActionBar(toolbar)

        var array = Database.getExpenses()
        var listView: ListView = findViewById(R.id.listExpenses)

        //creates adapter that uses items from earnings array and puts it into listview widget
        var adapter = ArrayAdapter(this, R.layout.fragment_entries_list, array)
        listView.adapter = adapter

        fab.setOnClickListener {
            startActivity(Intent(this, EditExpenseActivity::class.java))
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
