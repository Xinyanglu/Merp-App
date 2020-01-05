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

    private fun setup(){
        val entriesFolder = File(this.getExternalFilesDir(null), "/entries")
        if (!entriesFolder.exists()) entriesFolder.mkdirs()
        Log.d("directories", entriesFolder.absolutePath)

        val earningsFile = File(entriesFolder.absolutePath, "/earnings.txt")
        if (!earningsFile.exists()) earningsFile.createNewFile()
        Log.d("directories", earningsFile.absolutePath)

        val expensesFile = File(entriesFolder.absolutePath, "/expenses.txt")
        if (!expensesFile.exists()) expensesFile.createNewFile()
        Log.d("directories", expensesFile.absolutePath)

        val earningsSourcesFile = File(entriesFolder.absolutePath, "/earningsSources.txt")
        if (!earningsSourcesFile.exists()) earningsSourcesFile.createNewFile()
        Log.d("directories", earningsSourcesFile.absolutePath)

        val expensesSourcesFile = File(entriesFolder.absolutePath, "/expensesSources.txt")
        if (!expensesSourcesFile.exists()) expensesSourcesFile.createNewFile()
        Log.d("directories", expensesSourcesFile.absolutePath)

        Database.setDirectories(
            earningsFile.absolutePath,
            expensesFile.absolutePath,
            earningsSourcesFile.absolutePath,
            expensesSourcesFile.absolutePath
        )
        Database.readEarnings()
        Database.readExpenses()
        Database.readExpensesSources()
        Database.readEarningsSources()

        btnEarnings.setOnClickListener {
            startActivity(Intent(this, EarningsActivity::class.java))
        }

        btnExpenses.setOnClickListener {
            startActivity(Intent(this, ExpensesActivity::class.java))
        }

        btnReports.setOnClickListener {
            startActivity(Intent(this, ViewReportsActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            setup()
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
        }
    }

    //called whenever the user allows/denies a permission
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
            setup()
        }else{
            finishAndRemoveTask()
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