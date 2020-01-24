package com.merp.android.activities

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ListView
import com.google.android.material.snackbar.Snackbar
import com.merp.android.*
import kotlinx.android.synthetic.main.activity_earnings.*
import kotlinx.android.synthetic.main.fragment_entries.*
import kotlin.collections.ArrayList

/**
 * An [Activity] that allows the user to filter and scroll through a list of [Earning]s. and/or delete them.
 * The user can also navigate to [EditEarningActivity] to add/edit [Earning]s.
 */
class EarningsActivity : AppCompatActivity() {
    /** A [CustomAdapter] object that will take an array of [CustomListItem]s to put into the [ListView] */
    private lateinit var adapter: CustomAdapter

    /** An identifier code for communicating the user's intention to create a new earning to [EditEarningActivity] */
    private val newEarningCode = 101

    /** An identifier code for communicating the user's intention to edit their selected earning to [EditEarningActivity] */
    private val editEarningCode =  102

    /** Records the index in the array of the item the user selects for deletion */
    private var deleteIndex = 0

    /**
     * Sets up the functionality of the activity (adding listeners to TextViews, floating action buttons, ListViews, etc.).
     * Automatically called when the activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_earnings)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<EditText>(R.id.searchBarEntries).setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when(actionId){
                EditorInfo.IME_ACTION_SEARCH -> {
                    filterListView(searchBarEntries.text.toString())
                    hideKeyboard(v) //using an onClick method, thus param is required but (in this case) useless
                    true
                }
                else -> false
            }
        }

        //onClickListener for create new Earning button (floating action button)
        fab.setOnClickListener {
            val data = Intent(this, EditEarningActivity::class.java).apply {
                //add extra: NEW_EARNING_CODE to the Intent with key: "REQUEST_CODE"
                //extra is passed to the new activity and specifies that the user intends to create a new Earning
                putExtra("REQUEST_CODE", newEarningCode)
            }
            //this NEW_EARNING_CODE is used as the requestCode for the startActivityForResult method
            //when the user returns to this activity, onActivityResult will be called, and the requestCode will be NEW_EARNING_CODE
            startActivityForResult(data, newEarningCode)
        }
/*
        //keyword search feature for searchBarEntries EditText View
        searchBarEntries.addTextChangedListener{
            filterListView(it.toString())
        }*/

        //allows user to edit an Earning by short clicking an Earning in the ListView
        listEntries.setOnItemClickListener { parent, view, position, id ->
            val item = Database.getEarnings()[position]
            val year = item.getDate().getYear()
            val month = item.getDate().getMonth()
            val day = item.getDate().getDay()
            val source = item.getSource()
            val amount = item.getAmount()
            val addInfo = item.getAddInfo()

            val data = Intent(this, EditEarningActivity::class.java).apply {
                //passes the selected Earning's info to the new activity
                putExtra("EDIT_EARNING", "$year@$month@$day@$source@$amount@$addInfo@$position")
                //specifies that the user intends to edit their selected Earning
                putExtra("REQUEST_CODE", editEarningCode)
            }
            //when user returns to this activity, onActivityResults will be called with requestCode = EDIT_EARNING_CODE
            startActivityForResult(data, editEarningCode)
        }

        //when user long clicks an Earning in the ListView, deletion prompt pops up
        listEntries.setOnItemLongClickListener { parent, view, position, id ->
            deleteIndex = position
            val text = Database.getEarnings()[position].toString().replace(", ", "\n")
            textEntryInfo.text = text //shows the info of the selected Earning
            layoutDeleteEntry.visibility = View.VISIBLE
            true //required for some reason
        }

        //user clicks this button to cancel delete
        btnCancelDeleteEntry.setOnClickListener {
            layoutDeleteEntry.visibility = View.INVISIBLE
        }

        //user clicks this button to delete
        btnDeleteEntry.setOnClickListener {
            if(deleteIndex == -999){
                Database.getEarnings().clear()
            }else{
                Database.getEarnings().removeAt(deleteIndex)
            }
            Database.writeEarnings()
            layoutDeleteEntry.visibility = View.INVISIBLE
            updateListView(createCustomItemsList())
        }
    }

    /**
     * Updates the [ListView] to display all [Earning]s.
     * Automatically called when the activity is resumed.
     * Always called after [onCreate] as per the activity lifecycle.
     */
    override fun onResume() {
        super.onResume()
        updateListView(createCustomItemsList())
    }

    /** Automatically inflates the options menu to be a part of the toolbar. */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_earnings, menu)
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
                    putExtra("source", "EarningsActivity")
                }
                startActivity(data)
                return true
            }
            R.id.action_delete_earnings -> {
                textEntryInfo.text = resources.getString(R.string.text_delete_all_earnings) //shows the info of the selected Earning
                deleteIndex = -999
                layoutDeleteEntry.visibility = View.VISIBLE
                Database.writeEarnings()
                updateListView(createCustomItemsList())
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    /**
     * Creates a Snackbar message using data passed back from [EditEarningActivity].
     * * If the user created a new [Earning], displays its properties.
     * * If the user edited an [Earning], notifies the user that it updated successfully.
     *
     * Automatically called whenever the user returns to this activity if startActivityForResult was used.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if((requestCode == newEarningCode || requestCode == editEarningCode) && resultCode == Activity.RESULT_OK && data != null){
            var text = "" //records what the Snackbar message will be
            if(requestCode == newEarningCode) { //if a new Earning was created, set text varialbe to that Earning's info
                val result = data.getStringExtra("NEW_EARNING")
                if(result != null) {
                    val split = result.split("@")
                    val source = split[0]
                    val year = split[1]
                    val month = split[2]
                    val day = split[3]
                    val amount = split[4]
                    text = "New earning: \$$amount earned from $source ($year-$month-$day)"
                }
            }else{ //if an existing Earning was updated, sets text variable to the following message
                text = "Earning updated"
            }
            //creates and shows the Snackbar with the specified text
            Snackbar.make(
                findViewById(R.id.earningsLayoutActivity),
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
        adapter = CustomAdapter(this, R.layout.fragment_entries_list, list) //updates the adapter
        val listView: ListView = findViewById(R.id.listEntries) //creates an object for referencing the ListView
        listView.adapter = adapter //sets the ListView's adapter to the new adapter
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
     * Returns an ArrayList of [CustomListItem]s by converting each element of [Database.earnings] into a [CustomListItem].
     */
    private fun createCustomItemsList(): ArrayList<CustomListItem>{
        val info = Database.getEarnings()
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

    /*
    TODO():
    use Snackbar to display something along the lines of
    Earning deleted         UNDO
    after an earning is deleted
    give UNDO an onClickListener and program it to undo the delete?
    would require the "deleted" earning to be stored temporarily
    */
}

