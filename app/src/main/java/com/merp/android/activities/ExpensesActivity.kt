package com.merp.android.activities

import android.app.Activity
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

        fab.setOnClickListener {
            startActivityForResult(Intent(this, EditExpenseActivity::class.java), 888)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 888 && resultCode == Activity.RESULT_OK && data != null){
            val result = data.getStringExtra("NEW_EXPENSE")
            val split = result.split("@")
            val source = split[0]
            val year = split[1]
            val month = split[2]
            val day = split[3]
            val amount = split[4]

            Snackbar.make(findViewById(R.id.expensesLayout),
                    "New expense: \$$amount spent on $source ($year-$month-$day)",
                    Snackbar.LENGTH_LONG).show()
        }
    }

    private fun updateList(){
        val array = Database.getExpenses()
        val listView: ListView = findViewById(R.id.listExpenses)

        //creates adapter that uses items from expenses array and puts it into listview widget
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
