package com.merp.android.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ListView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.merp.android.*
import kotlinx.android.synthetic.main.activity_expenses.*
import kotlinx.android.synthetic.main.fragment_entries.*
import kotlin.collections.ArrayList

/**
 * An [Activity] that allows the user to filter and scroll through a list of [Expense]s. and/or delete them.
 * The user can also navigate to [EditExpenseActivity] to add/edit [Expense]s.
 */
class ExpensesActivity : AppCompatActivity() {
    /** A [CustomAdapter] object that will take an array of [CustomListItem]s to put into the [ListView] */
    private lateinit var adapter: CustomAdapter

    /** An identifier code for communicating the user's intention to create a new expense to [EditExpenseActivity] */
    private val newExpenseCode = 201

    /** An identifier code for communicating the user's intention to edit their selected expense to [EditExpenseActivity] */
    private val editExpenseCode = 202

    /**
     * Sets up the functionality of the activity (adding listeners to TextViews, floating action buttons, ListViews, etc.).
     * Automatically called when the activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expenses)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var deleteIndex = 0

        fab.setOnClickListener {
            val data = Intent(this, EditExpenseActivity::class.java).apply {
                putExtra("REQUEST_CODE", newExpenseCode)
            }
            startActivityForResult(data, newExpenseCode)
        }

        searchBarEntries.addTextChangedListener {
            filterListView(it.toString())
        }

        listEntries.setOnItemClickListener { parent, view, position, id ->
            val item = Database.getExpenses()[position]
            val year = item.getDate().getYear()
            val month = item.getDate().getMonth()
            val day = item.getDate().getDay()
            val source = item.getSource()
            val amount = item.getAmount()
            val addInfo = item.getAddInfo()

            val data = Intent(this, EditExpenseActivity::class.java).apply {
                putExtra("EDIT_EXPENSE", "$year@$month@$day@$source@$amount@$addInfo@$position")
                putExtra("REQUEST_CODE", editExpenseCode)
            }
            startActivityForResult(data, editExpenseCode)
        }

        listEntries.setOnItemLongClickListener { parent, view, position, id ->
            deleteIndex = position
            textEntryInfo.text = Database.getExpenses()[position].toString()
            layoutDeleteEntry.visibility = View.VISIBLE
            true
        }

        btnCancelDeleteEntry.setOnClickListener {
            layoutDeleteEntry.visibility = View.INVISIBLE
        }

        btnDeleteEntry.setOnClickListener {
            Database.getExpenses().removeAt(deleteIndex)
            Database.writeExpenses()
            layoutDeleteEntry.visibility = View.INVISIBLE
            updateListView(createCustomItemsList())
        }
    }

    /**
     * Updates the [ListView] to display all [Expense]s.
     * Automatically called when the activity is resumed.
     * Always called after [onCreate] as per the activity lifecycle.
     */
    override fun onResume() {
        super.onResume()
        updateListView(createCustomItemsList())
    }

    /** Automatically inflates the options menu to be a part of the toolbar. */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_expenses, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * Depending on the item clicked, performs a specific task.
     * Automatically called whenever the user clicks an options menu item.
     *
     * @param [item] The options [MenuItem] that the user clicked
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_display_help -> {
                val data = Intent(this, HelpActivity::class.java).apply{
                    putExtra("source", "ExpensesActivity")
                }
                startActivity(data)
                return true
            }
            R.id.action_delete_expenses -> {
                Database.getExpenses().clear()
                Database.writeExpenses()
                updateListView(createCustomItemsList())
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    /**
     * Creates a Snackbar message using data passed back from [EditExpenseActivity].
     * * If the user created a new [Expense], displays its properties.
     * * If the user edited an [Expense], notifies the user that it updated successfully.
     *
     * Automatically called whenever the user returns to this activity if startActivityForResult was used.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if((requestCode == newExpenseCode || requestCode == editExpenseCode) && resultCode == Activity.RESULT_OK && data != null){
            var text = ""
            if(requestCode == newExpenseCode) {
                val result = data.getStringExtra("NEW_EXPENSE")
                if(result != null){
                    val split = result.split("@")
                    val source = split[0]
                    val year = split[1]
                    val month = split[2]
                    val day = split[3]
                    val amount = split[4]
                    text = "New expense: \$$amount spent on $source ($year-$month-$day)"
                }
            }else{
                text = "Expense updated"
            }
            Snackbar.make(
                findViewById(R.id.expensesLayout),
                text,
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    /**
     * Updates the [ListView] to display a new list based on the given [list] of [CustomListItem]s.
     *
     * @param [list] An ArrayList of [CustomListItem]s that will be passed into the [CustomAdapter]
     */
    private fun updateListView(list: ArrayList<CustomListItem>){
        adapter = CustomAdapter(this, R.layout.fragment_entries_list, list)
        val listView: ListView = findViewById(R.id.listEntries)
        listView.adapter = adapter
    }

    /**
     * Filters the [ListView] to display only those that contain user-specified keyword(s).
     *
     * @param [query] The keyword(s) that the user types into the search bar
     */
    private fun filterListView(query: String){
        if(query.contains(",")){
            val queries = query.split(",")
            filterMulti(queries)
        }else{
            filterSingle(query)
        }
    }

    /**
     * Filters the array of [CustomListItem]s for those that contain the given keyword.
     * Calls [updateListView] when done.
     *
     * @param [query] A single keyword that the user is searching for
     */
    private fun filterSingle(query: String){
        val filteredItems = createCustomItemsList()

        //from the last element of the array to the first, checks if the element contains the keyword
        //if not, removes the element from the array
        for(i in (filteredItems.size-1) downTo 0){
            val item = filteredItems[i].toString()
            if (!item.contains(query, ignoreCase = true)) {
                filteredItems.removeAt(i)
            }
        }
        //updates the ListView with the filtered array
        updateListView(filteredItems)
    }

    /**
     * Filters the array of [CustomListItem]s for those that contain all given keywords.
     * Calls [updateListView] when done.
     *
     * @param [queries] A List of keywords that the user is searching for
     */
    private fun filterMulti(queries: List<String>){
        val filteredItems = createCustomItemsList()
        //from the last element of the array to the first, checks if the element contains the keyword
        //if not, removes the element from the array
        for(i in (filteredItems.size-1) downTo 0){
            var containsAll = true
            for(j in queries){ //check if the item contains all queries
                //if the query starts with spaces, remove those spaces
                while(j.startsWith(" ")){
                    j.replaceFirst(" ", "")
                }
                val item = filteredItems[i].toString()
                if (!item.contains(j, ignoreCase = true)) {
                    containsAll = false
                }
            }
            if(!containsAll){
                filteredItems.removeAt(i)
            }
        }
        //updates the ListView with the filtered array
        updateListView(filteredItems)
    }

    /**
     * Returns an ArrayList of [CustomListItem]s by converting each element of [Database.expenses] into a [CustomListItem].
     */
    private fun createCustomItemsList(): ArrayList<CustomListItem>{
        val info = Database.getExpenses()
        val customItems = ArrayList<CustomListItem>()

        for(i in info){
            val split = i.toFile().split("@")
            customItems.add(CustomListItem(split[0], split[1], "$" + split[2], split[3]))
        }
        return customItems
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
}
