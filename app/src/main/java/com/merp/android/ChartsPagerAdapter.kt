package com.merp.android

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.merp.android.activities.ChartFragment

class ChartsPagerAdapter(fragmentManager: FragmentManager, private val charts: ArrayList<ChartFragment>):
    FragmentPagerAdapter(fragmentManager){

    override fun getItem(position: Int): Fragment {
        return charts[position]
    }

    override fun getCount(): Int {
        return charts.size
    }
}