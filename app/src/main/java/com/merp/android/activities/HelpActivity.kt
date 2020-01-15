package com.merp.android.activities

import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.merp.android.R

import kotlinx.android.synthetic.main.activity_help.*
import kotlinx.android.synthetic.main.content_help.*

class HelpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val source = intent.getStringExtra("source")
        val text: Int

        /*
        TODO: improve text formatting in xml file using tags like
         <font size="VALUE"></font>
         <b></b>        bold?
         <u></u>        underline?
         <i></i>        italicize?
         use &#8226; to create a "bullet point" (syntactic sugar, does not actually make a list itself, so need to utilize \n)
         */
        text = when(source){
            "EarningsActivity" -> R.string.help_earnings_activity
            "EditEarningActivity" -> R.string.help_edit_earnings_activity
            "EarningsSourcesActivity" -> R.string.help_earnings_sources_activity //TODO
            "ExpensesActivity" -> R.string.help_expenses_activity
            "EditExpenseActivity" -> R.string.help_edit_expense_activity
            "ExpensesSourcesActivity" -> R.string.help_expenses_sources_activity //TODO
            else -> -999 //required, but should never be reached
        }
        textHelp.setText(text)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
