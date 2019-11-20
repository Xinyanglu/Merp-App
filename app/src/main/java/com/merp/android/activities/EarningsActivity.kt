package com.merp.android.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.merp.android.R
import android.content.Intent
import android.widget.ArrayAdapter
import android.widget.ListView
import com.merp.android.Database

import kotlinx.android.synthetic.main.activity_earnings.*

class EarningsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_earnings)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        updateList()

        fab.setOnClickListener {
            startActivity(Intent(this, EditEarningActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        updateList()
    }

    private fun updateList(){
        val array = Database.getEarnings()
        val listView: ListView = findViewById(R.id.listEarnings)

        //creates adapter that uses items from earnings array and puts it into listview widget
        val adapter = ArrayAdapter(this, R.layout.fragment_entries_list, array)
        listView.adapter = adapter
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
        ^perhaps when you swipe an Earning, a button to delete appears on the side?
    */
}
