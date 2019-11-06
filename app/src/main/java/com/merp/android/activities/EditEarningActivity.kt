package com.merp.android.activities

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.merp.android.R
import kotlinx.android.synthetic.main.activity_edit_earning.*
import android.widget.Spinner
import android.widget.ArrayAdapter
import com.merp.android.Database
import com.merp.android.Date
import kotlinx.android.synthetic.main.content_edit_earning.*
import java.math.BigDecimal


class EditEarningActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_earning)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //spinnerSources (dropdown menu for sources)
        val dropdownSources: Spinner = findViewById(R.id.spinnerSource)
        dropdownSources.onItemSelectedListener
        //TODO(): replace values, create a way to get sources from Database (create an ArrayList of sources in Database?)
        val sources = arrayListOf<String>("", "Job", "Human trafficking", "Lottery")
        val adapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sources)
        dropdownSources.adapter = adapter
        //floating action button (save earning button)
        fab.setOnClickListener {
            var hasErrors = false //TODO(): expand if statements to set hasErrors to true
            //check if required fields are filled
            if(enterDate.text.isEmpty()) enterDate.error = "Date required"
            if(spinnerSource.selectedItemId.toInt() == 0) textSource.error = "Source required" //TODO(): find a way to actually display the error message (currently does not properly display)
            else textSource.error = null //required as this will not be done automatically
            if(enterAmount.text.isEmpty()) enterAmount.error = "Amount required"

            if(!hasErrors){
                val year = enterDate.text.substring(0, 4).toInt()
                val month = enterDate.text.substring(5, 7).toInt()
                val day = enterDate.text.substring(8, 10).toInt()
                Database.addEarning(Date(year, month, day), spinnerSource.selectedItem.toString(), BigDecimal(enterAmount.text.toString()), enterAddInfo.text.toString())
            }

            //DEBUGGING - check all information related to the first Earning object in the array
            //error checking (hasErrors variable) not implemented; will likely accept empty values ("" or null?)
            val firstEarning = Database.earning[0]
            val debug = "Date: ${firstEarning.getDate()}, Source: ${firstEarning.getSource()}, Amount: \$${firstEarning.getAmount()}, Additional Info: ${firstEarning.getAddInfo()}"
            Log.d("EditEarningActivity", debug)
        }
    }

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
