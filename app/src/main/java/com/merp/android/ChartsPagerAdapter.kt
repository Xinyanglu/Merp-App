package com.merp.android

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.merp.android.activities.ChartFragment

/**
 * A custom [FragmentPagerAdapter] class.
 *
 *
 * idk what you want to put here i just wanted to add that nice little link kekw
 */
class ChartsPagerAdapter(fragmentManager: FragmentManager, private val charts: ArrayList<ChartFragment>):
    FragmentPagerAdapter(fragmentManager){

    /**
     * An array of the titles of each tab.
     * Used in [getPageTitle] to set the title of that tab.
     */
    private val tabTitles = arrayOf("Earnings Bar", "Earnings Pie", "Expenses Bar", "Expenses Pie")

    override fun getItem(position: Int): Fragment {
        return charts[position]
    }

    override fun getCount(): Int {
        return charts.size
    }

    /**
     * Returns the title of the tab at a given position.
     * Used in the default [FragmentPagerAdapter] class to set the title of that tab.
     *
     * @param [position] The position of the tab (starting from the leftmost tab at position 0)
     */
    override fun getPageTitle(position: Int): CharSequence? {
        return tabTitles[position]
    }
}