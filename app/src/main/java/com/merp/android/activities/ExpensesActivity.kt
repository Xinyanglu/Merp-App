package com.merp.android.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ListView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.merp.android.*
import kotlinx.android.synthetic.main.activity_expenses.*
import kotlinx.android.synthetic.main.fragment_entries.*
import java.util.*
import kotlin.collections.ArrayList

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
            filterList(it.toString())
        }

        listEntries.setOnItemClickListener { parent, view, position, id ->
            val item = Database.getExpenses()[position]
            val year = item.getDate().getYear()
            val month = item.getDate().getMonth()
            val day = item.getDate().getDay()
            val source = item.getSource()
            val amount = item.getAmount()
            val addInfo = item.getAddInfo()

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
            updateList(createCustomItemsList())
        }
    }

    override fun onResume() {
        super.onResume()
        updateList(createCustomItemsList())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_expenses, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if((requestCode == NEW_EXPENSE_CODE || requestCode == EDIT_EXPENSE_CODE) && resultCode == Activity.RESULT_OK && data != null){
            var text = ""
            if(requestCode == NEW_EXPENSE_CODE) {
                val result = data.getStringExtra("NEW_EXPENSE")
                if(result != null){
                    val split = result.split("@")
                    val source = split[0]
                    val year = split[1]
                    val month = split[2]
                    val day = split[3]
                    val amount = split[4]
                    text = "New expense: \$$amount spent on $source ($year-$month-$day)"
                }
            }else{
                text = "Expense updated"
            }
            Snackbar.make(
                findViewById(R.id.expensesLayout),
                text,
                Snackbar.LENGTH_INDEFINITE
            ).show()
        }
    }

    private fun updateList(list: ArrayList<CustomListItem>){
        adapter = CustomAdapter(this, R.layout.fragment_entries_list, list)
        val listView: ListView = findViewById(R.id.listEntries)
        listView.adapter = adapter
    }

    private fun filterList(query: String){
        val filteredItems = createCustomItemsList()

        for(i in (filteredItems.size-1) downTo 0){
            val item = filteredItems[i]
            val itemInfo = (item.getTVDate() + item.getTVSource() + item.getTVAmount() + item.getTVAddInfo()).toLowerCase(
                Locale.US)
            if(!itemInfo.contains(query.toLowerCase(Locale.US))){
                filteredItems.removeAt(i)
            }
        }
        updateList(filteredItems)
    }

    private fun createCustomItemsList(): ArrayList<CustomListItem>{
        val info = Database.getExpenses()
        val customItems = ArrayList<CustomListItem>()

        for(i in info){
            val split = i.toFile().split("@")
            customItems.add(CustomListItem(split[0], split[1], "$" + split[2], split[3]))
        }
        return customItems
    }

    fun menuItemClicked(item: MenuItem){
        Log.d("Expenses toolbar", "$item clicked")

        when(item.itemId){
            R.id.action_delete_expenses -> {
                Database.getExpenses().clear()
                Database.writeExpenses()
                updateList(createCustomItemsList())
            }
        }
    }

    fun hideKeyboard(v: View){
        val imm: InputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
    }
}
