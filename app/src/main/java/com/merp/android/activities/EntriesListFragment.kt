package com.merp.android.activities


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.merp.android.R

/**
 * The Kotlin class for the fragment used for the ListView in [EarningsActivity] and [ExpensesActivity].
 */
class EntriesListFragment : Fragment() {

    /**
     * Inflates the layout for this fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_entries_list, container, false)
    }
}
