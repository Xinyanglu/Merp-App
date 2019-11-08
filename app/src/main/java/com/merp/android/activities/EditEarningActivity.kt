package com.merp.android.activities

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import com.merp.android.Database
import com.merp.android.Date
import com.merp.android.R
import kotlinx.android.synthetic.main.activity_edit_earning.*
import kotlinx.android.synthetic.main.content_edit_earning.*
import kotlinx.android.synthetic.main.fragment_entry.*
import java.math.BigDecimal


class EditEarningActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_earning)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //floating action button (save earning button)
        fab.setOnClickListener {
            var hasErrors = false


            /**
             * TODO(): add date selection widget (calendar)
             * perhaps prevent user from entering a date via text box
             * if so, enterDate error checking can be removed
             *
             * otherwise, add error checking for months and days:
             * January - 31 days
             * February - 28 days in a common year and 29 days in leap years
             * March - 31 days
             * April - 30 days
             * May - 31 days
             * June - 30 days
             * July - 31 days
             * August - 31 days
             * September - 30 days
             * October - 31 days
             * November - 30 days
             * December - 31 days
             */
            //check if required fields are filled
            if(enterDate.text.isEmpty()){ //TODO(): add date selection widget (calendar)
                enterDate.error = "Date required"
                hasErrors = true
            }else if(enterDate.text.length != 10 ||
                    !enterDate.text.substring(0, 4).isDigitsOnly() ||
                    !enterDate.text.substring(5, 7).isDigitsOnly() ||
                    !enterDate.text.substring(8, 10).isDigitsOnly() ||
                    enterDate.text[4] != '-' ||
                    enterDate.text[7] != '-'){
                enterDate.error = "YYYY-MM-DD format required"
            }

            if(spinnerSource.selectedItemId.toInt() == 0){
                textSource.error = "Source required"
                hasErrors = true
            } //TODO(): find a way to actually display the error message (currently does not properly display)
            else textSource.error = null //required as this will not be done automatically

            if(enterAmount.text.isEmpty()){
                enterAmount.error = "Amount required"
                hasErrors = true
            }

            if(!hasErrors){
                val year = enterDate.text.substring(0, 4).toInt()
                val month = enterDate.text.substring(5, 7).toInt()
                val day = enterDate.text.substring(8, 10).toInt()
                Database.addEarning(Date(year, month, day), spinnerSource.selectedItem.toString(), BigDecimal(enterAmount.text.toString()), enterAddInfo.text.toString())
            }

            //DEBUGGING - check all information related to the first Earning object in the array
            //error checking (hasErrors variable) not implemented; will likely accept empty values ("" or null?)
            /*val firstEarning = Database.earning[0]
            val debug = "Date: ${firstEarning.getDate().getFullDate()}, Source: ${firstEarning.getCategory()}, Amount: \$${firstEarning.getAmount()}, Additional Info: ${firstEarning.getAddInfo()}"
            Log.d("EditEarningActivity", debug)*/
        }

        val dropdownSources: Spinner = findViewById(R.id.spinnerSource)
        dropdownSources.onItemSelectedListener
        //TODO(): replace values, create a way to get sources from Database (create an ArrayList of sources in Database?)
        val sources = arrayListOf<String>("", "Job", "Human trafficking", "Lottery")
        val adapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sources)
        dropdownSources.adapter = adapter
    }

    /*override fun onStart() {
        super.onStart()


    }*/

    //hides the keyboard
    private fun hideSoftKeyboard(activity: Activity){
        val imm: InputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
    }

    //when the background is clicked, call a method to hide the keyboard
    fun backgroundClicked(v: View){
        hideSoftKeyboard(this)
    }
}