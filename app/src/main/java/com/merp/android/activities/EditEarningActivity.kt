package com.merp.android.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import com.merp.android.Database
import com.merp.android.Date
import com.merp.android.R
import kotlinx.android.synthetic.main.activity_edit_earning.*
import kotlinx.android.synthetic.main.fragment_edit_entry.*
import java.math.BigDecimal

/**
 * An [Activity] for adding/editing earnings to/from [Database.earnings].
 * The user can also navigate to [EarningsSourcesActivity].
 */
class EditEarningActivity : AppCompatActivity() {
    /** An identifier code that checks if the user navigated to this activity with the intention to create a new earning */
    private val newEarningCode = 101

    /** An identifier code that checks if the user navigated to this activity with the intention to edit their selected earning */
    private val editEarningCode = 102

    /** If the user selected an earning from [EarningsActivity] to edit, this records the index of that earning in [Database.earnings] */
    private var index = -999

    /**
     * Records the request (identifier) code passed from [EarningsActivity]
     * and is compared against [newEarningCode] and [editEarningCode] to determine the user's intention.
     */
    private var requestCode = -999

    /** A [DatePicker] object used for selecting the [com.merp.android.Earning.date] */
    private lateinit var dp: DatePicker

    /**
     * Sets up the activity (inflating layout, setting text, adding listeners to floating action buttons, etc.).
     *
     * Automatically called when the activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_earning)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        textAmount.text = resources.getString(R.string.text_amount_earned)
        dp = findViewById(R.id.datePicker)

        requestCode = intent.getIntExtra("REQUEST_CODE", -999)

        //TODO(): change button icon to be a pencil (or something more "edit"-like) in the xml file?
        btnEditSources.setOnClickListener{
            startActivity(Intent(this, EarningsSourcesActivity::class.java))
        }

        //floating action button (save earning button)
        fab.setOnClickListener {
            var hasErrors = false
            var numDecimalPlaces = 0

            if(spinnerSource.isEmpty()){
                textSource.requestFocus()
                textSource.error = "Source required"
                hasErrors = true
            }else{
                textSource.error = null
            }

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

            /** -------------------------------------------------------------------------
             * TODO: app does not save the very first entry after installation???
             * --------------------------------------------------------------------------
             */

            //DatePicker indexes months starting at 0 (January), therefore +1
            if(!hasErrors){
                //if additional info has unnecessary line breaks at beginning, remove them
                //if additional info is all line breaks, set additional info to ""
                if(enterAddInfo.text.contains("\n")){
                    var temp = enterAddInfo.text.toString()
                    while(temp.startsWith("\n")){
                        temp = temp.replaceFirst("\n", "")
                    }
                    enterAddInfo.setText(temp)
                }

                val source = spinnerSource.selectedItem.toString()
                val year = dp.year
                val month = dp.month+1
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

                if(requestCode == editEarningCode){
                    val earning = Database.getEarnings()[index]
                    earning.setDate(Date(year, month, day))
                    earning.setSource(source)
                    earning.setAmount(BigDecimal(amount))
                    earning.setAddInfo(addInfo)
                    Database.writeEarnings()
                }else if(requestCode == newEarningCode) {
                    Database.addEarning(
                        Date(year, month, day),
                        source,
                        BigDecimal(amount),
                        enterAddInfo.text.toString()
                    )
                    //pass data to EarningsActivity where it will be used for Snackbar
                    data.putExtra("NEW_EARNING", "$source@$year@$month@$day@$amount")
                }
                setResult(Activity.RESULT_OK, data)
                finish()
            }
        }
    }

    /**
     * Calls [setSources].
     * If the [requestCode] is [editEarningCode], sets all the layout components to display the properties of the selected earning.
     *
     * Called by default when the activity is resumed.
     * Always called after [onCreate] as per the activity lifecycle.
     */
    override fun onResume() {
        super.onResume()
        setSources()

        //if user is editing an existing earning, sets all layout components to display that earning's info
        if(requestCode == editEarningCode) {
            val result = intent.getStringExtra("EDIT_EARNING")
            if (result != null) {
                val split = result.split("@")
                val year = split[0].toInt()
                val month = split[1].toInt() - 1 //month index for DatePicker starts at 0 for January
                val day = split[2].toInt()
                val source = split[3]
                val amount = split[4]
                val addInfo = split[5]
                index = split[6].toInt()

                dp.updateDate(year, month, day)
                for (i in 0 until spinnerSource.count) {
                    if (spinnerSource.getItemAtPosition(i).toString() == source) {
                        spinnerSource.setSelection(i)
                        break
                    }
                }
                enterAmount.setText(amount)
                enterAddInfo.setText(addInfo)
            }
        }
    }

    /** Automatically inflates the options menu to be a part of the toolbar. */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit_earning, menu)
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
                putExtra("source", "EditEarningActivity")
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
    fun onClick(v: View){
        val imm: InputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
    }

    /**
     * Updates the spinner (dropdown menu) to display and allow the user to select any of the [Database.earningsSources].
     */
    private fun setSources(){
        //create object for referencing the spinner (dropdown menu) of sources
        val dropdownSources: Spinner = findViewById(R.id.spinnerSource)
        //create an array of sources
        val sources: ArrayList<String> = Database.getEarningsSources()
        //use an ArrayAdapter for adapting an array to a spinner
        val adapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sources)
        //set the adapter of the spinner to the ArrayAdapter
        dropdownSources.adapter = adapter
    }

    /*TODO(): figure out how to get this to work then implement it for EditExpenseActivity
    private fun dimForeground(dim: Boolean){
        val fragment = supportFragmentManager.findFragmentById(R.id.editEntryFragment) as EditEntryFragment
        fragment.dimForeground(dim)
    }*/
}