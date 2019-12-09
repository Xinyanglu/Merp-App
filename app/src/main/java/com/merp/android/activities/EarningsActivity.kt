package com.merp.android.activities

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.merp.android.R
import android.content.Intent
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.merp.android.Database
import com.merp.android.Earning
import kotlinx.android.synthetic.main.activity_earnings.*
import kotlinx.android.synthetic.main.fragment_entries.*

class EarningsActivity : AppCompatActivity() {
    private lateinit var adapter: ArrayAdapter<Earning>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_earnings)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var deleteIndex = 0 //records which item to delete

        fab.setOnClickListener {
            startActivityForResult(Intent(this, EditEarningActivity::class.java), 999)
        }

        searchBarEntries.addTextChangedListener{
            this.adapter.filter.filter(it)
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
            updateList()
        }
    }

    override fun onResume() {
        super.onResume()
        updateList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_earnings, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 999 && resultCode == Activity.RESULT_OK && data != null){
            val result = data.getStringExtra("NEW_EARNING")
            val split = result.split("@")
            val source = split[0]
            val year = split[1]
            val month = split[2]
            val day = split[3]
            val amount = split[4]

            Snackbar.make(findViewById(R.id.earningsLayout),
                    "New earning: \$$amount earned from $source ($year-$month-$day)",
                    Snackbar.LENGTH_LONG).show()
        }
    }

    private fun updateList(){
        val array = Database.getEarnings()

        val listView: ListView = findViewById(R.id.listEntries)

        //creates adapter that uses items from earnings array and puts it into listview widget
        adapter = ArrayAdapter(this, R.layout.fragment_entries_list, array) //TODO(): use 4 parameter constructor: ArrayAdapter(this, R.layout.<layout>, R.id.<TextView>, array)
        listView.adapter = adapter
    }

    fun menuItemClicked(item: MenuItem){
        Log.d("Earnings toolbar", "$item clicked")

        when(item.itemId){
            R.id.action_settings -> println() //TODO(): will this be needed?
            R.id.action_delete_earnings -> {
                Database.getEarnings().clear()
                Database.writeEarnings()
                updateList()
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

