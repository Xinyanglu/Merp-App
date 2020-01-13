package com.merp.android.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.merp.android.Database
import com.merp.android.R
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    /**
     * Sets up the layout of the activity.
     * Creates the folder and text files if they do not exist, then sets their directories and reads the files into their corresponding arrays.
     * Called only if the user grants external read/write permissions, otherwise this method is never reached and the app is closed.
     */
    private fun setup(){
        //set up the folder
        val entriesFolder = File(this.getExternalFilesDir(null), "/entries")
        if (!entriesFolder.exists()) entriesFolder.mkdirs()
        Log.d("directories", entriesFolder.absolutePath)

        //set up the file for recording earnings
        val earningsFile = File(entriesFolder.absolutePath, "/earnings.txt")
        if (!earningsFile.exists()) earningsFile.createNewFile()
        Log.d("directories", earningsFile.absolutePath)

        //set up the file for recording expenses
        val expensesFile = File(entriesFolder.absolutePath, "/expenses.txt")
        if (!expensesFile.exists()) expensesFile.createNewFile()
        Log.d("directories", expensesFile.absolutePath)

        //set up the file for recording sources of earnings
        val earningsSourcesFile = File(entriesFolder.absolutePath, "/earningsSources.txt")
        if (!earningsSourcesFile.exists()) earningsSourcesFile.createNewFile()
        Log.d("directories", earningsSourcesFile.absolutePath)

        //set up the file for recording sources of expenses
        val expensesSourcesFile = File(entriesFolder.absolutePath, "/expensesSources.txt")
        if (!expensesSourcesFile.exists()) expensesSourcesFile.createNewFile()
        Log.d("directories", expensesSourcesFile.absolutePath)

        //set the files' directories for referencing for file IO
        Database.setDirectories(
            earningsFile.absolutePath,
            expensesFile.absolutePath,
            earningsSourcesFile.absolutePath,
            expensesSourcesFile.absolutePath
        )

        //read the contents of the files into their corresponding arrays
        Database.readEarnings()
        Database.readExpenses()
        Database.readExpensesSources()
        Database.readEarningsSources()

        //when "Edit Earnings" button is pressed, start EarningsActivity
        btnEarnings.setOnClickListener {
            startActivity(Intent(this, EarningsActivity::class.java))
        }

        //when "Edit Expenses" button is pressed, start ExpensesActivity
        btnExpenses.setOnClickListener {
            startActivity(Intent(this, ExpensesActivity::class.java))
        }

        //when "View Reports" button is pressed, start DatePickerActivity (for selecting the date range for the report)
        btnReports.setOnClickListener {
            startActivity(Intent(this, DatePickerActivity::class.java))
        }
    }

    //automatically called when the activity is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //set title of main activity programmatically as setting it via android:label XML attribute
        // in the manifest will cause errors with the display name of the app (actual name of the app will remain unchanged)
        this.title = resources.getString(R.string.title_activity_main)

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            setup()
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
        }
    }

    //automatically called whenever the user allows/denies a permission
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(grantResults[0] == PackageManager.PERMISSION_GRANTED){   //if user grants permissions, set up the app
            setup()
        }else{                                                      //else, close the app
            finishAndRemoveTask()
        }
    }

    //automatically called when the activity is resumed
    //always called after onCreate() as per the activity lifecycle
    override fun onResume() {
        super.onResume()
        setDate()
    }

    /**
     * Sets the text of mainDate TextView to the system date.
     */
    private fun setDate(){
        val sdf = SimpleDateFormat("yyyy, MMMM dd", Locale.US)
        //TODO: getDateInstance()??? instead of SimpleDateFormat
        mainDate.text = sdf.format(Date())
    }
}