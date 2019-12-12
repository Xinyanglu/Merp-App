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
import com.merp.android.*

import kotlinx.android.synthetic.main.activity_expenses.*
import kotlinx.android.synthetic.main.fragment_entries.*

class ExpensesActivity : AppCompatActivity() {
    private lateinit var adapter: CustomAdapter
    private val NEW_EXPENSE_CODE = 201
    private val EDIT_EXPENSE_CODE = 202

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expenses)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var deleteIndex = 0

        fab.setOnClickListener {
            val data = Intent(this, EditExpenseActivity::class.java).apply {
                putExtra("REQUEST_CODE", NEW_EXPENSE_CODE)
            }
            startActivityForResult(data, NEW_EXPENSE_CODE)
        }

        searchBarEntries.addTextChangedListener {
            this.adapter.filter.filter(it)
        }

        listEntries.setOnItemClickListener { parent, view, position, id ->
            val item = Database.getExpenses()[position]

            val year = item.getDate().getYear()
            val month = item.getDate().getMonth()
            val day = item.getDate().getDay()
            val source = item.getSource()
            val amount = item.getAmount()
            var addInfo: String
            try{
                addInfo = item.getAddInfo()
            }catch(e: Exception){
                addInfo = ""
            }

            val data = Intent(this, EditExpenseActivity::class.java).apply {
                putExtra("EDIT_EXPENSE", "$year@$month@$day@$source@$amount@$addInfo@$position")
                putExtra("REQUEST_CODE", EDIT_EXPENSE_CODE)
            }
            startActivityForResult(data, EDIT_EXPENSE_CODE)
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

        if((requestCode == NEW_EXPENSE_CODE || requestCode == EDIT_EXPENSE_CODE) && resultCode == Activity.RESULT_OK && data != null){
            val text: String
            if(requestCode == NEW_EXPENSE_CODE) {
                val result = data.getStringExtra("NEW_EXPENSE")
                val split = result.split("@")
                val source = split[0]
                val year = split[1]
                val month = split[2]
                val day = split[3]
                val amount = split[4]
                text = "New expense: \$$amount spent on $source ($year-$month-$day)"
            }else{
                text = "Expense updated"
            }
            Snackbar.make(findViewById(R.id.expensesLayout),
                    text,
                    Snackbar.LENGTH_INDEFINITE).show()
        }
    }

    private fun updateList(){
        val dates = Database.getEveryExpensesDate()
        val sources = Database.getEveryExpensessSource()
        val amounts = Database.getEveryExpensesAmount()
        val addInfo = Database.getEveryExpensesAddInfo()

        val customItems = ArrayList<CustomListItem>()
        for(i in 0 until dates.size){
            customItems.add(CustomListItem(dates[i].getFullDate(), sources[i], "\$${amounts[i]}", addInfo[i]))
        }
        adapter = CustomAdapter(this, R.layout.fragment_entries_list, customItems)
        val listView: ListView = findViewById(R.id.listEntries)
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
