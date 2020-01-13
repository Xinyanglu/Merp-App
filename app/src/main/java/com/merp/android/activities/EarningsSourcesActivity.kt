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

/**
 * An [Activity] for searching through and adding/deleting [Database.earningsSources].
 * Displays all sources of earnings in a [ListView] which can be filtered for keyword(s) via a search bar.
 */
class EarningsSourcesActivity : AppCompatActivity() {
    /** An [ArrayAdapter] object that will take an array of Strings to put into the [ListView] */
    private lateinit var adapter: ArrayAdapter<String>

    /**
     * Sets up the functionality of the activity (adding listeners to TextViews, ListViews, buttons, etc.).
     *
     * Automatically called when the activity is created.
     */
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
            when{
                enterNewSource.text.isEmpty() -> {
                    enterNewSource.error = "Field is empty"
                }
                Database.getEarningsSources().contains(enterNewSource.text.toString()) -> {
                    enterNewSource.error = "This source already exists"
                }
                else -> {
                    if(enterNewSource.text.startsWith(" ")){
                        var temp = enterNewSource.text.toString()
                        while(temp.startsWith(" ")){
                            temp = temp.replaceFirst(" ", "")
                        }
                        enterNewSource.setText(temp)
                    }
                    Database.addEarningsSource(enterNewSource.text.toString())
                    Snackbar.make(findViewById(R.id.earningsSourcesLayout), "New source: \"${enterNewSource.text}\" added", Snackbar.LENGTH_LONG).show()
                    enterNewSource.text.clear()
                    updateListView()
                }
            }
        }

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
            updateListView()
        }
    }

    /**
     * Calls [updateListView].
     * Automatically called when the activity is resumed.
     * Always called after [onCreate] as per the activity lifecycle.
     */
    override fun onResume() {
        super.onResume()
        updateListView()
    }

    /**
     * When the background is clicked, hides the on-screen keyboard.
     * NOTE: as this is an XML onClick attribute method, a View parameter is required even if unused.
     *
     * @param [v] The [View] clicked
     */
    fun hideKeyboard(v: View){
        val imm: InputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
    }

    /**
     * Updates the [ListView] to display all the elements of [Database.earningsSources].
     */
    private fun updateListView(){
        val array = Database.getEarningsSources()
        val listView: ListView = findViewById(R.id.listSources)

        //creates adapter that takes items from array and puts them into a ListView
        adapter = ArrayAdapter(this, R.layout.fragment_sources_list, array)
        listView.adapter = adapter
    }
}
