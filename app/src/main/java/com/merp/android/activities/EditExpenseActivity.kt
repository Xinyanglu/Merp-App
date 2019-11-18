package com.merp.android.activities

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Spinner
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.merp.android.Database
import com.merp.android.Date
import com.merp.android.R

import kotlinx.android.synthetic.main.activity_edit_expense.*
import kotlinx.android.synthetic.main.fragment_edit_entry.*
import java.math.BigDecimal

class EditExpenseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_expense)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        textAmount.text = resources.getString(R.string.text_amount_spent)
        val dp: DatePicker = findViewById(R.id.datePicker)
        setSources()

        //TODO(): change button icon to be a pencil (or something more "edit"-like) in the xml file?
        btnEditSources.setOnClickListener{
            layoutAddSource.visibility = View.VISIBLE
        }

        btnAddSource.setOnClickListener {
            if(enterNewSource.text.isEmpty()){
                enterNewSource.error = "Field is empty"
            }else{
                //TODO(): add <enterNewSource.text.toString()> to ArrayList of sources

                setSources()
                Log.d("EditExpenseActivity", enterNewSource.text.toString())
            }
        }

        fab.setOnClickListener {
            var hasErrors = false

            if(spinnerSource.selectedItemId.toInt() == 0){
                spinnerError.requestFocus()
                spinnerError.error = "Source required"
                hasErrors = true
            } //TODO(): find a way to actually display the error message (currently does not properly display)
            else textSource.error = null //required as this will not be done automatically

            if(enterAmount.text.isEmpty()){
                enterAmount.error = "Amount required"
                hasErrors = true
            }

            //DatePicker indexes months starting at 0 (January), therefore +1
            if(!hasErrors){
                Database.addExpense(Date(dp.year, dp.month+1, dp.dayOfMonth), spinnerSource.selectedItem.toString(), BigDecimal(enterAmount.text.toString()), enterAddInfo.text.toString())

                //DEBUGGING - check if all the information of every stored expense is actually stored and retrievable
                //INFORMATION IS PROPERLY STORED (Nov. 12 ~10:40 p.m.)
                for(i in Database.expense){
                    Log.d("Entered Expenses", i.toString())
                }
            }
        }
    }

    //when the background is clicked, hides keyboard and layoutAddSource
    //onClick methods (called from xml file) must have one and only one parameter of View type
    fun onClick(v: View){
        val imm: InputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)

        enterNewSource.error = null
        layoutAddSource.visibility = View.INVISIBLE
    }

    private fun setSources(){
        //spinner (dropdown menu) for sources
        val dropdownSources: Spinner = findViewById(R.id.spinnerSource)
        dropdownSources.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //do nothing
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                spinnerError.error = null
            }
        }

        //TODO(): replace values, create a way to get sources from Database (create an ArrayList of sources in Database?)
        val sources = arrayListOf("CHOOSE SOURCE", "Food", "Utilities", "Entertainment")
        val adapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sources)
        dropdownSources.adapter = adapter
    }
}
