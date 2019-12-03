package com.merp.android.activities

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.merp.android.Database
import com.merp.android.R

import kotlinx.android.synthetic.main.activity_expenses_sources.*
import kotlinx.android.synthetic.main.fragment_sources.*

class ExpensesSourcesActivity : AppCompatActivity() {
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expenses_sources)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var deleteIndex = 0

        enterNewSource.addTextChangedListener{
            this.adapter.filter.filter(it)
        }

        btnAddSource.setOnClickListener{
            if(enterNewSource.text.isEmpty()){
                enterNewSource.error = "Field is empty"
            }else{
                Database.addExpensesSource(enterNewSource.text.toString())
                Snackbar.make(findViewById(R.id.expensesSourcesLayout), "New source: \"${enterNewSource.text}\" added", Snackbar.LENGTH_LONG).show()
                enterNewSource.text.clear()
                updateList()
            }
        }

        listSources.setOnItemLongClickListener { parent, view, position, id ->
            deleteIndex = position //record which item was long clicked (its index in the array)
            textConfirm.text = Database.getExpensesSources()[position]
            layoutDeleteSource.visibility = View.VISIBLE
            true // -> Boolean required for some reason
        }

        btnCancel.setOnClickListener {
            layoutDeleteSource.visibility = View.INVISIBLE
        }

        btnDeleteSource.setOnClickListener {
            Database.getExpensesSources().removeAt(deleteIndex) //erase source from array
            Database.writeExpensesSources() //erase source from text file
            layoutDeleteSource.visibility = View.INVISIBLE
            updateList()
        }
    }

    override fun onResume() {
        super.onResume()
        updateList()
    }

    fun onClick(vi: View){
        val imm: InputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
    }

    private fun updateList(){
        val array = Database.getExpensesSources()
        val listView: ListView = findViewById(R.id.listSources)

        //creates adapter that uses items from earnings array and puts it into listview widget
        adapter = ArrayAdapter(this, R.layout.fragment_sources_list, array)
        listView.adapter = adapter
    }
}
