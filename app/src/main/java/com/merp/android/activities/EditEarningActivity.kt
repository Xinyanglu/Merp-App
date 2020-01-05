package com.merp.android.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
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

class EditEarningActivity : AppCompatActivity() {
    private val NEW_EARNING_CODE = 101
    private val EDIT_EARNING_CODE = 102
    private var index = -999
    private var requestCode = -999
    private lateinit var dp: DatePicker

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
                /*spinnerError.requestFocus()
                spinnerError.error = "Source required"*/
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

            /**
             * TODO: app does not save the very first entry after installation???
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

                if(requestCode == EDIT_EARNING_CODE){
                    val earning = Database.getEarnings()[index]
                    earning.setDate(Date(year, month, day))
                    earning.setSource(source)
                    earning.setAmount(BigDecimal(amount))
                    earning.setAddInfo(addInfo)
                    Database.writeEarnings()
                }else if(requestCode == NEW_EARNING_CODE) {
                    Database.addEarning(
                        Date(year, month, day),
                        source,
                        BigDecimal(amount),
                        enterAddInfo.text.toString())
                    Log.d("HELLOTHARE", Database.getEarnings().toString())
                    //pass data to EarningsActivity where it will be used for Snackbar
                    data.putExtra("NEW_EARNING", "$source@$year@$month@$day@$amount")
                }
                setResult(Activity.RESULT_OK, data)
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setSources()

        if(requestCode == EDIT_EARNING_CODE){
            val result = intent.getStringExtra("EDIT_EARNING")
            val split = result.split("@")
            val year = split[0].toInt()
            val month = split[1].toInt() - 1 //month index for DatePicker starts at 0 for January
            val day = split[2].toInt()
            val source = split[3]
            val amount = split[4]
            val addInfo = split[5]
            index = split[6].toInt()

            dp.updateDate(year, month, day)
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