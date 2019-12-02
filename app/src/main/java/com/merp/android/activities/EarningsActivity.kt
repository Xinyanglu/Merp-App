package com.merp.android.activities

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.merp.android.R
import android.content.Intent
import android.text.Layout
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.android.material.snackbar.Snackbar
import com.merp.android.Database

import kotlinx.android.synthetic.main.activity_earnings.*

class EarningsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_earnings)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fab.setOnClickListener {
            startActivityForResult(Intent(this, EditEarningActivity::class.java), 999)
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

        if(requestCode == 999){
            if(resultCode == Activity.RESULT_OK){
                if(data != null) {
                    val result = data.getStringExtra("NEW_EARNING")
                    val split = result.split("@")
                    val source = split[0]
                    val year = split[1]
                    val month = split[2]
                    val day = split[3]
                    val amount = split[4]

                    Snackbar.make(findViewById(R.id.earningsLayout),
                            "New earning: $amount from $source ($year-$month-$day)",
                            Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun updateList(){
        val array = Database.getEarnings()
        val listView: ListView = findViewById(R.id.listEarnings)

        //creates adapter that uses items from earnings array and puts it into listview widget
        val adapter = ArrayAdapter(this, R.layout.fragment_entries_list, array)
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
