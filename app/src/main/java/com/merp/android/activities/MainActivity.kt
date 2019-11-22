package com.merp.android.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.merp.android.Database
import com.merp.android.R
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setDate()

        //TODO(): check if these permission requests are necessary
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }

        //---------------------THE FILE IO THING----------------------
        val entriesFolder = File(Environment.getExternalStorageDirectory(), "/entries")
        if(!entriesFolder.exists()) entriesFolder.mkdirs()

        val earningsFile = File(entriesFolder.absolutePath, "/earnings.txt")
        if(!earningsFile.exists()) earningsFile.createNewFile()

        val expensesFile = File(entriesFolder.absolutePath, "/expenses.txt")
        if(!expensesFile.exists()) expensesFile.createNewFile()

        val earningsSourcesFile = File(entriesFolder.absolutePath, "/earningsSources.txt")
        if(!earningsSourcesFile.exists()) earningsSourcesFile.createNewFile()

        val expensesSourcesFile = File(entriesFolder.absolutePath, "/expensesSources.txt")
        if(!expensesSourcesFile.exists()) expensesSourcesFile.createNewFile()

        Database.setDirectories(earningsFile.absolutePath, expensesFile.absolutePath, earningsSourcesFile.absolutePath, expensesSourcesFile.absolutePath)
        Database.readEarnings()
        Database.readExpenses()

        btnEarnings.setOnClickListener {
            startActivity(Intent(this, EarningsActivity::class.java))
        }

        btnExpenses.setOnClickListener {
            startActivity(Intent(this, ExpensesActivity::class.java))
        }

        btnReports.setOnClickListener {
            TODO()
        }
    }

    override fun onResume() {
        super.onResume()
        setDate()
    }

    private fun setDate(){
        val sdf = SimpleDateFormat("yyyy, MMMM dd", Locale.getDefault())
        mainDate.text = sdf.format(Date())
    }
}

/**
 * TODO
 * perhaps change this activity's layout to have a drawer(?) functionality?
 * get the drawer to display an expandable list view to display things like
 * Earnings>View Earnings/Edit Earnings/Edit Sources of Earnings
 * Expenses>View Expenses/Edit Expenses/Edit Sources of Expenses
 * Reports>View Reports/???
 */