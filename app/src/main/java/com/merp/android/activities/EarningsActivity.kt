package com.merp.android.activities

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.merp.android.R
import android.content.Intent

import kotlinx.android.synthetic.main.activity_earnings.*

class EarningsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_earnings)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            startActivity(Intent(this, TODO()))
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
