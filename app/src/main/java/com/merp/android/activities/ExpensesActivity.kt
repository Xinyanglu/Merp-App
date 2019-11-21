package com.merp.android.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        updateList()

        fab.setOnClickListener {
            startActivity(Intent(this, EditExpenseActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        updateList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_expenses, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun updateList(){
        val array = Database.getExpenses()
        val listView: ListView = findViewById(R.id.listExpenses)

        //creates adapter that uses items from earnings array and puts it into listview widget
        val adapter = ArrayAdapter(this, R.layout.fragment_entries_list, array)
        listView.adapter = adapter
    }

    fun menuItemClicked(item: MenuItem){
        Log.d("Expenses toolbar", "$item clicked")

        when(item.itemId){
            R.id.action_delete_expenses -> {
                Database.getExpenses().clear()
                Database.writeExpenses()
                updateList()
            }
        }
    }
}
