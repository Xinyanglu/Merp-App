package com.merp.android.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import com.merp.android.Database
import com.merp.android.Date
import com.merp.android.R
import kotlinx.android.synthetic.main.activity_edit_earning.*
import kotlinx.android.synthetic.main.fragment_edit_entry.*
import java.math.BigDecimal


class EditEarningActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_earning)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        textAmount.text = resources.getString(R.string.text_amount_earned)
        val dp: DatePicker = findViewById(R.id.datePicker)

        //TODO(): change button icon to be a pencil (or something more "edit"-like) in the xml file?
        btnEditSources.setOnClickListener{
            startActivity(Intent(this, EarningsSourcesActivity::class.java))
            Log.d("EditEarningActivity", "btnEditSources clicked")
        }

        //floating action button (save earning button)
        fab.setOnClickListener {
            var hasErrors = false

            if(spinnerSource.isEmpty()){
                spinnerError.requestFocus()
                spinnerError.error = "Source required"
                hasErrors = true
            }
            else textSource.error = null //required as this will not be done automatically

            if(enterAmount.text.isEmpty()){
                enterAmount.error = "Amount required"
                hasErrors = true
            }

            //DatePicker indexes months starting at 0 (January), therefore +1
            if(!hasErrors){
                val source = spinnerSource.selectedItem.toString()
                val year = dp.year
                val month = dp.month+1
                val day = dp.dayOfMonth
                val amount = enterAmount.text.toString()
                Database.addEarning(Date(year, month, day), source, BigDecimal(amount), enterAddInfo.text.toString())

                //pass data to EarningsActivity, where it will be used for Snackbar
                val data = Intent()
                data.putExtra("NEW_EARNING", "$source@$year@$month@$day@$amount")
                setResult(Activity.RESULT_OK, data)

                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setSources()
    }

    //when the background is clicked, hides keyboard and layoutAddSource
    //onClick methods (called from xml file) must have one and only one parameter of View type
    fun onClick(v: View){
        val imm: InputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
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

        val sources: ArrayList<String> = Database.getEarningsSources()
        val adapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sources)
        dropdownSources.adapter = adapter
    }

    //TODO(): figure out how to get this to work then implement it for EditExpenseActivity
    private fun dimForeground(dim: Boolean){
        val fragment = supportFragmentManager.findFragmentById(R.id.editEntryFragment) as EditEntryFragment
        fragment.dimForeground(dim)
    }
}