package com.merp.android.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import com.merp.android.Database
import com.merp.android.Date
import com.merp.android.R
import kotlinx.android.synthetic.main.activity_edit_expense.*
import kotlinx.android.synthetic.main.fragment_edit_entry.*
import java.math.BigDecimal

/**
 * An [Activity] for adding/editing expenses to/from [Database.expenses].
 * The user can also navigate to [ExpensesSourcesActivity].
 */
class EditExpenseActivity : AppCompatActivity() {
    /** An identifier code that checks if the user navigated to this activity with the intention to create a new expense */
    private val newExpenseCode = 201

    /** An identifier code that checks if the user navigated to this activity with the intention to edit their selected expense */
    private val editExpenseCode = 202

    /** If the user selected an expense from [ExpensesActivity] to edit, this records the index of that expense in [Database.expenses] */
    private var index = -999

    /**
     * Records the request (identifier) code passed from [ExpensesActivity]
     * and is compared against [newExpenseCode] and [editExpenseCode] to determine the user's intention.
     */
    private var requestCode = -999

    /** A [DatePicker] object used for selecting the [com.merp.android.Expense.date] */
    private lateinit var dp: DatePicker

    /**
     * Sets up the activity (inflating layout, setting text, adding listeners to floating action buttons, etc.).
     *
     * Automatically called when the activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_expense)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        textAmount.text = resources.getString(R.string.text_amount_spent)
        dp = findViewById(R.id.datePicker)

        //get the requestCode passed from ExpensesActivity
        //will be used for deciding whether to perform new expense functionalities or edit expense ones
        requestCode = intent.getIntExtra("REQUEST_CODE", -999)

        //TODO(): change button icon to be a pencil (or something more "edit"-like) in the xml file?
        //when clicked, starts ExpensesSourcesActivity
        btnEditSources.setOnClickListener{
            startActivity(Intent(this, ExpensesSourcesActivity::class.java))
        }

        //floating action button (save expense button)
        fab.setOnClickListener {
            var hasErrors = false
            var numDecimalPlaces = 0

            //error if no source selected
            if(spinnerSource.isEmpty()){
                textSource.requestFocus()
                textSource.error = "Source required"
                hasErrors = true
            }else{
                //required as this will not be done automatically
                textSource.error = null
            }

            //error if text is empty or has more than 2 decimal places
            if(enterAmount.text.isEmpty()){
                enterAmount.error = "Amount required"
                hasErrors = true
            }else if(enterAmount.text.contains(".")) {
                val index = enterAmount.text.indexOf(".")+1 //index after the decimal (to find number of decimal places)
                numDecimalPlaces = enterAmount.text.substring(index).length
                if(numDecimalPlaces > 2){ //if more than 2 decimal places
                    enterAmount.error = "Max 2 decimal places"
                    hasErrors = true
                }
            }

            //if no errors, get all the values from the layout components (date, source, amount, add. info)
            if(!hasErrors){
                //if additional info has unnecessary line breaks at beginning, remove them
                //if additional info is all line breaks, set additional info to ""
                var temp = enterAddInfo.text.toString()
                while(temp.startsWith("\n")){
                    temp = temp.replaceFirst("\n", "")
                }
                enterAddInfo.setText(temp)
                while(temp.startsWith(" ")){
                    temp = temp.replaceFirst(" ", "")
                }
                enterAddInfo.setText(temp)

                val source = spinnerSource.selectedItem.toString()
                val year = dp.year
                val month = dp.month+1 //DatePicker indexes months starting at 0 (Jan), therefore +1
                val day = dp.dayOfMonth

                var amount = enterAmount.text.toString()
                if(!amount.contains(".")){
                    amount += "."
                }
                //for consistency, make all amounts have 2 decimal places
                for(i in 0 until (2-numDecimalPlaces)){
                    amount += "0"
                }

                val addInfo = enterAddInfo.text.toString()
                val data = Intent()

                if(requestCode == editExpenseCode){ //if user is editing an expense, overwrite that expense's properties
                    val expense = Database.getExpenses()[index]
                    expense.setDate(Date(year, month, day))
                    expense.setSource(source)
                    expense.setAmount(BigDecimal(amount))
                    expense.setAddInfo(addInfo)
                    Database.writeExpenses()
                }else if(requestCode == newExpenseCode){ //if user is adding a new expense, add it to the ArrayList
                    Database.addExpense(
                        Date(year, month, day),
                        source,
                        BigDecimal(amount),
                        enterAddInfo.text.toString()
                    )
                    //pass data to ExpensesActivity where it will be used for Snackbar
                    data.putExtra("NEW_EXPENSE", "$source@$year@$month@$day@$amount")
                }
                setResult(Activity.RESULT_OK, data)
                //finish this activity and return to ExpensesActivity
                finish()
            }
        }
    }

    /**
     * Calls [setSources].
     * If the [requestCode] is [editExpenseCode], sets all the layout components to display the properties of the selected expense.
     *
     * Called by default when the activity is resumed.
     * Always called after [onCreate] as per the activity lifecycle.
     */
    override fun onResume() {
        super.onResume()
        setSources()

        //if user is editing and existing expense, sets all layout components to display that expense's info
        if(requestCode == editExpenseCode){
            val result = intent.getStringExtra("EDIT_EXPENSE")
            val split = result.split("@")
            val year = split[0].toInt()
            val month = split[1].toInt() - 1 //month index for DatePicker starts at 0 for January
            val day = split[2].toInt()
            val source = split[3]
            val amount = split[4]
            val addInfo = split[5]
            index = split[6].toInt()

            dp.updateDate(year, month, day)

            //find the expense's source and set the spinner to display that source
            for(i in 0 until spinnerSource.count){
                if(spinnerSource.getItemAtPosition(i).toString() == source){
                    spinnerSource.setSelection(i)
                    break
                }
            }
            enterAmount.setText(amount)
            enterAddInfo.setText(addInfo)
        }
    }

    /** Automatically inflates the options menu to be a part of the toolbar. */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit_expense, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * Depending on the item clicked, performs a specific task.
     * Automatically called whenever the user clicks an options menu item.
     *
     * @param [item] The options [MenuItem] that the user clicked
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_display_help){
            val data = Intent(this, HelpActivity::class.java).apply{
                putExtra("source", "EditExpenseActivity")
            }
            startActivity(data)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * When the background is clicked, hides the on-screen keyboard.
     * NOTE: as this is an XML onClick attribute method, a View parameter is required even if unused.
     *
     * @param [v] The [View] clicked
     */
    fun hideKeyboard(v: View){
        val imm: InputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
    }

    /**
     * Updates the spinner (dropdown menu) to display and allow the user to select any of the [Database.expensesSources].
     */
    private fun setSources(){
        //create object for referencing the spinner (dropdown menu) of sources
        val dropdownSources: Spinner = findViewById(R.id.spinnerSource)
        //create an array of sources
        val sources: ArrayList<String> = Database.getExpensesSources()
        //use an ArrayAdapter for adapting an array to a spinner
        val adapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sources)
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
        //set the adapter of the spinner to the ArrayAdapter
        dropdownSources.adapter = adapter
    }
}
