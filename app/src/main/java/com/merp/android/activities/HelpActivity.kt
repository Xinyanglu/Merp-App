package com.merp.android.activities

import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.merp.android.R

import kotlinx.android.synthetic.main.activity_help.*
import kotlinx.android.synthetic.main.content_help.*

/**
 * An Activity that displays various instructions for the user depending on which Activity
 * they navigated to this one from.
 */
class HelpActivity : AppCompatActivity() {

    /**
     * Sets the text to display the instructions for how to navigate the activity that the user came from.
     * 
     * Automatically called when the activity is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val source = intent.getStringExtra("source")
        val text: Int

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

    /**
     * Depending on the item clicked, performs a specific task.
     * Automatically called whenever the user clicks an options menu item.
     *
     * @param [item] The options [MenuItem] that the user clicked
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> { //when user presses the back arrow button, returns to the previous
                finish()           //activity without recreating it (prevents user-entered data from
                return true        //being erased if they navigate to this activity before saving it)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
