package com.merp.android.activities

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.DatePicker
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

        val dp: DatePicker = findViewById(R.id.datePicker)

        val dropdownSources: Spinner = findViewById(R.id.spinnerSource)
        dropdownSources.onItemSelectedListener
        //TODO(): replace values, create a way to get sources from Database (create an ArrayList of sources in Database?)
        val sources = arrayListOf("", "Job", "Misusing donation funds", "Lottery")
        val adapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sources)
        dropdownSources.adapter = adapter

        //floating action button (save earning button)
        fab.setOnClickListener {
            var hasErrors = false

            if(spinnerSource.selectedItemId.toInt() == 0){
                textSource.error = "Source required"
                hasErrors = true
            } //TODO(): find a way to actually display the error message (currently does not properly display)
            else textSource.error = null //required as this will not be done automatically

            if(enterAmount.text.isEmpty()){
                enterAmount.error = "Amount required"
                hasErrors = true
            }

            //DatePicker indexes months starting at 0 (January), therefore +1
            if(!hasErrors){
                Database.addEarning(Date(dp.year, dp.month+1, dp.dayOfMonth), spinnerSource.selectedItem.toString(), BigDecimal(enterAmount.text.toString()), enterAddInfo.text.toString())
            }

            //DEBUGGING - check all information related to the first Earning object in the array
            //error checking (hasErrors variable) not implemented; will likely accept empty values ("" or null?)
            val firstEarning = Database.earning[0]
            val debug = "Date: ${firstEarning.getDate().getFullDate()}, Source: ${firstEarning.getCategory()}, Amount: \$${firstEarning.getAmount()}, Additional Info: ${firstEarning.getAddInfo()}"
            Log.d("EditEarningActivity", debug)
        }
    }

    //when the background is clicked hides the keyboard
    //onClick methods (called from xml file) must have one and only one parameter of View type
    fun hideKeyboard(v: View){
        val imm: InputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
    }
}