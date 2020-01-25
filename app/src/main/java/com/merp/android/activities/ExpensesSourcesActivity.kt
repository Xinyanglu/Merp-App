package com.merp.android.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

/**
 * An [Activity] for searching through and adding/deleting [Database.expensesSources].
 * Displays all sources of expenses in a [ListView] which can be filtered for keyword(s) via a search bar.
 */
class ExpensesSourcesActivity : AppCompatActivity() {
    /** An [ArrayAdapter] object that will take an array of Strings to put into the [ListView] */
    private lateinit var adapter: ArrayAdapter<String>

    /**
     * Sets up the functionality of the activity (adding listeners to TextViews, ListViews, buttons, etc.).
     *
     * Automatically called when the activity is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expenses_sources)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //records which item to delete
        var deleteIndex = 0

        //every time the text changes, filters list for sources that match the contents of the EditText view
        enterNewSource.addTextChangedListener{
            this.adapter.filter.filter(it)
        }

        //if there are no errors, adds teh contents of the EditText as a new source
        btnAddSource.setOnClickListener{
            if(enterNewSource.text.isEmpty()){
                enterNewSource.error = "Field is empty"
            }else if(Database.getExpensesSources().contains(enterNewSource.text.toString())){
                enterNewSource.error = "This source already exists"
            }else{ //if no errors, add source
                if(enterNewSource.text.startsWith(" ")){
                    var temp = enterNewSource.text.toString()
                    while(temp.startsWith(" ")){
                        temp = temp.replaceFirst(" ", "")
                    }
                    enterNewSource.setText(temp)
                }
                //add the source to the ArrayList
                Database.addExpensesSource(enterNewSource.text.toString())
                //use a Snackbar message to inform the user that the source was added
                Snackbar.make(findViewById(R.id.expensesSourcesLayout), "New source: \"${enterNewSource.text}\" added", Snackbar.LENGTH_LONG).show()
                enterNewSource.text.clear()
                //update the ListView to display the newly-added source
                updateListView()
            }
        }

        //when an item in the ListView is long clicked, prompt the user to confirm deletion of that item
        listSources.setOnItemLongClickListener { parent, view, position, id ->
            deleteIndex = position //record which item was long clicked (its index in the array)
            textSourceInfo.text = Database.getExpensesSources()[position]
            layoutDeleteSource.visibility = View.VISIBLE
            true // -> Boolean required for some reason
        }

        //cancel delete
        btnCancelDeleteSource.setOnClickListener {
            layoutDeleteSource.visibility = View.INVISIBLE
        }

        //confirm delete and update ListView
        btnDeleteSource.setOnClickListener {
            Database.getExpensesSources().removeAt(deleteIndex) //erase source from array
            Database.writeExpensesSources() //erase source from text file
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

    /** Automatically inflates the options menu to be a part of the toolbar. */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_expenses_sources, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * Depending on the item clicked, performs a specific task.
     * Automatically called whenever the user clicks an options menu item.
     *
     * @param [item] The options [MenuItem] that the user clicked
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_display_help){
            val data = Intent(this, HelpActivity::class.java).apply{
                putExtra("source", "ExpensesSourcesActivity")
            }
            startActivity(data)
            return true
        }
        return super.onOptionsItemSelected(item)
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
     * Updates the [ListView] to display all the elements of [Database.expensesSources].
     */
    private fun updateListView(){
        val array = Database.getExpensesSources()
        val listView: ListView = findViewById(R.id.listSources)

        //creates adapter that takes items from array and puts them into a ListView
        adapter = ArrayAdapter(this, R.layout.fragment_sources_list, array)
        //sets the ListView's adapter to the ArrayAdapter
        listView.adapter = adapter
    }
}
