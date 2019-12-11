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
import kotlinx.android.synthetic.main.activity_earnings_sources.*
import kotlinx.android.synthetic.main.fragment_sources.*

class EarningsSourcesActivity : AppCompatActivity() {
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_earnings_sources)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var deleteIndex = 0 //records which item to delete

        //filters list for sources that match the contents of the EditText view
        enterNewSource.addTextChangedListener{
            this.adapter.filter.filter(it)
        }

        btnAddSource.setOnClickListener{
            //TODO(): error checking for existing sources of the same name
            if(enterNewSource.text.isEmpty()) {
                enterNewSource.error = "Field is empty"
            }else if(Database.getEarningsSources().contains(enterNewSource.text.toString())){
                enterNewSource.error = "This source already exists"
            }else{
                Database.addEarningsSource(enterNewSource.text.toString())
                Snackbar.make(findViewById(R.id.earningsSourcesLayout), "New source: \"${enterNewSource.text}\" added", Snackbar.LENGTH_LONG).show()
                enterNewSource.text.clear()
                updateList()
            }
        }

        //TODO(): allow user to delete source even if there are existing earnings from that source?
        listSources.setOnItemLongClickListener { parent, view, position, id ->
            deleteIndex = position //record which item was long clicked (its index in the array)
            textSourceInfo.text = Database.getEarningsSources()[position]
            layoutDeleteSource.visibility = View.VISIBLE
            true // -> Boolean required for some reason
        }

        btnCancelDeleteSource.setOnClickListener {
            layoutDeleteSource.visibility = View.INVISIBLE
        }

        btnDeleteSource.setOnClickListener {
            Database.getEarningsSources().removeAt(deleteIndex) //erase source from array
            Database.writeEarningsSources() //erase source from text file
            layoutDeleteSource.visibility = View.INVISIBLE
            updateList()
        }
    }

    override fun onResume() {
        super.onResume()
        updateList()
    }

    fun onClick(v: View){
        val imm: InputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
    }

    private fun updateList(){
        val array = Database.getEarningsSources()
        val listView: ListView = findViewById(R.id.listSources)

        //creates adapter that uses items from earnings array and puts it into listview widget
        adapter = ArrayAdapter(this, R.layout.fragment_sources_list, array)
        listView.adapter = adapter
    }
}
