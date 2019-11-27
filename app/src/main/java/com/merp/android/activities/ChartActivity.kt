package com.merp.android.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.merp.android.R

class ChartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        var intent = getIntent()
        var extras = Bundle()
        extras.putString("entry", intent.getStringExtra("entry"))
        extras.putString("method", intent.getStringExtra("method"))
        var chartFragment = ChartFragment()
        chartFragment.setArguments(extras)

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,chartFragment).commit()
    }
}
