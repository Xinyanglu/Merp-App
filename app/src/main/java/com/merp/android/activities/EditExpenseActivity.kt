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

class EditExpenseActivity : AppCompatActivity() {
    private val newExpenseCode = 201
    private val editExpenseCode = 202
    private var index = -999
    private var requestCode = -999
    private lateinit var dp: DatePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_expense)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        textAmount.text = resources.getString(R.string.text_amount_spent)
        dp = findViewById(R.id.datePicker)

        requestCode = intent.getIntExtra("REQUEST_CODE", -999)

        //TODO(): change button icon to be a pencil (or something more "edit"-like) in the xml file?
        btnEditSources.setOnClickListener{
            startActivity(Intent(this, ExpensesSourcesActivity::class.java))
        }

        fab.setOnClickListener {
            var hasErrors = false
            var numDecimalPlaces = 0

            if(spinnerSource.isEmpty()){
                textSource.requestFocus()
                textSource.error = "Source required"
                hasErrors = true
            }else{
                //required as this will not be done automatically
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

                if(requestCode == editExpenseCode){
                    val expense = Database.getExpenses()[index]
                    expense.setDate(Date(year, month, day))
                    expense.setSource(source)
                    expense.setAmount(BigDecimal(amount))
                    expense.setAddInfo(addInfo)
                    Database.writeExpenses()
                }else if(requestCode == newExpenseCode) {
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
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setSources()

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit_expense, menu)
        return super.onCreateOptionsMenu(menu)
    }

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

    //when the background is clicked, hides keyboard and layoutAddSource
    //onClick methods (called from xml file) must have one and only one parameter of View type
    fun onClick(v: View){
        val imm: InputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
    }

    private fun setSources(){
        //spinner (dropdown menu) for sources
        val dropdownSources: Spinner = findViewById(R.id.spinnerSource)
        val sources: ArrayList<String> = Database.getExpensesSources()
        val adapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sources)
        dropdownSources.adapter = adapter
    }
}
