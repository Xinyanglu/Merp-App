package com.merp.android.activities

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar
import com.merp.android.*
import kotlinx.android.synthetic.main.activity_earnings.*
import kotlinx.android.synthetic.main.fragment_edit_entry.*
import kotlinx.android.synthetic.main.fragment_entries.*
import java.lang.Exception
import java.lang.NullPointerException
import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList

class EarningsActivity : AppCompatActivity() {
    private lateinit var adapter: CustomAdapter
    private val NEW_EARNING_CODE = 101
    private val EDIT_EARNING_CODE =  102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_earnings)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var deleteIndex = 0 //records which item to delete

        fab.setOnClickListener {
            val data = Intent(this, EditEarningActivity::class.java).apply {
                putExtra("REQUEST_CODE", NEW_EARNING_CODE)
            }
            startActivityForResult(data, NEW_EARNING_CODE)
        }

        searchBarEntries.addTextChangedListener{
            //this.adapter.filter.filter(it)
            filterList(it.toString())
        }

        listEntries.setOnItemClickListener { parent, view, position, id ->
            val item = Database.getEarnings()[position]

            val year = item.getDate().getYear()
            val month = item.getDate().getMonth()
            val day = item.getDate().getDay()
            val source = item.getSource()
            val amount = item.getAmount()
            var addInfo: String
            //try {
                addInfo = item.getAddInfo()/*
            }catch(e: Exception){
                addInfo = ""
            }*/


            val data = Intent(this, EditEarningActivity::class.java).apply {
                putExtra("EDIT_EARNING", "$year@$month@$day@$source@$amount@$addInfo@$position")
                putExtra("REQUEST_CODE", EDIT_EARNING_CODE)
            }
            startActivityForResult(data, EDIT_EARNING_CODE)
        }

        listEntries.setOnItemLongClickListener { parent, view, position, id ->
            deleteIndex = position
            textEntryInfo.text = Database.getEarnings()[position].toString()
            layoutDeleteEntry.visibility = View.VISIBLE
            true //required for some reason
        }

        btnCancelDeleteEntry.setOnClickListener {
            layoutDeleteEntry.visibility = View.INVISIBLE
        }

        btnDeleteEntry.setOnClickListener {
            Database.getEarnings().removeAt(deleteIndex)
            Database.writeEarnings()
            layoutDeleteEntry.visibility = View.INVISIBLE
            updateList(createCustomItemsList())
        }
    }

    override fun onResume() {
        super.onResume()
        updateList(createCustomItemsList())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_earnings, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if((requestCode == NEW_EARNING_CODE || requestCode == EDIT_EARNING_CODE) && resultCode == Activity.RESULT_OK && data != null){
            val text: String
            if(requestCode == NEW_EARNING_CODE) {
                val result = data.getStringExtra("NEW_EARNING")
                val split = result.split("@")
                val source = split[0]
                val year = split[1]
                val month = split[2]
                val day = split[3]
                val amount = split[4]
                text = "New earning: \$$amount earned from $source ($year-$month-$day)"
            }else{
                text = "Earning updated"
            }
            Snackbar.make(findViewById(R.id.earningsLayout),
                    text,
                    Snackbar.LENGTH_INDEFINITE).show()
        }
    }

    private fun updateList(list: ArrayList<CustomListItem>){
        /*
        val dates = Database.getEveryEarningsDate()
        val sources = Database.getEveryEarningsSource()
        val amounts = Database.getEveryEarningsAmount()
        val addInfo = Database.getEveryEarningsAddInfo()


        val customItems = ArrayList<CustomListItem>()
        for(i in 0 until dates.size){
            customItems.add(CustomListItem(dates[i].getFullDate(), sources[i], "\$${amounts[i]}", addInfo[i]))
        }*/

        adapter = CustomAdapter(this, R.layout.fragment_entries_list, list)
        val listView: ListView = findViewById(R.id.listEntries)
        listView.adapter = adapter
    }

    private fun filterList(query: String){
        val filteredItems = createCustomItemsList()

        for(i in (filteredItems.size-1) downTo 0){
            val item = filteredItems[i]
            val itemInfo = (item.getTVDate() + item.getTVSource() + item.getTVAmount() + item.getTVAddInfo()).toLowerCase(Locale.US)
            if(!itemInfo.contains(query.toLowerCase(Locale.US))){
                filteredItems.removeAt(i)
            }
        }
        updateList(filteredItems)
    }

    private fun createCustomItemsList(): ArrayList<CustomListItem>{
        val info = Database.getEarnings()
        val customItems = ArrayList<CustomListItem>()

        for(i in info){
            val split = i.toFile().split("@")
            customItems.add(CustomListItem(split[0], split[1], "$" + split[2], split[3]))
        }
        return customItems
    }

    fun menuItemClicked(item: MenuItem){
        Log.d("Earnings toolbar", "$item clicked")

        when(item.itemId){
            R.id.action_settings -> println() //TODO(): will this be needed?
            R.id.action_delete_earnings -> {
                Database.getEarnings().clear()
                Database.writeEarnings()
                updateList(createCustomItemsList())
            }
        }
    }

    fun hideKeyboard(v: View){
        val imm: InputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
    }

    /*
    TODO():
    use Snackbar to display something along the lines of
    Earning deleted         UNDO
    after an earning is deleted
    give UNDO an onClickListener and program it to undo the delete?
    would this require the "deleted" earning to be stored temporarily?

    how should earnings be deleted
    delete via a delete (trashcan?) button in EditEarningsActivity?
    delete via swiping an Earning in the list of earnings?
    perhaps when you swipe an Earning, a button to delete appears on the side?
    */
}

