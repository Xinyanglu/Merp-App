package com.merp.android.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.merp.android.Database
import com.merp.android.Expense
import com.merp.android.R

import kotlinx.android.synthetic.main.activity_expenses.*
import kotlinx.android.synthetic.main.fragment_entries.*

class ExpensesActivity : AppCompatActivity() {
    private lateinit var adapter: ArrayAdapter<Expense>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expenses)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var deleteIndex = 0

        fab.setOnClickListener {
            startActivityForResult(Intent(this, EditExpenseActivity::class.java), 888)
        }

        searchBarEntries.addTextChangedListener {
            this.adapter.filter.filter(it)
        }

        listEntries.setOnItemLongClickListener { parent, view, position, id ->
            deleteIndex = position
            textEntryInfo.text = Database.getExpenses()[position].toString()
            layoutDeleteEntry.visibility = View.VISIBLE
            true
        }

        btnCancelDeleteEntry.setOnClickListener {
            layoutDeleteEntry.visibility = View.INVISIBLE
        }

        btnDeleteEntry.setOnClickListener {
            Database.getExpenses().removeAt(deleteIndex)
            Database.writeExpenses()
            layoutDeleteEntry.visibility = View.INVISIBLE
            updateList()
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
        val listView: ListView = findViewById(R.id.listEntries)

        //creates adapter that uses items from expenses array and puts it into listview widget
        adapter = ArrayAdapter(this, R.layout.fragment_entries_list, array)
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

    fun hideKeyboard(v: View){
        val imm: InputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
    }
}
