package com.merp.android.activities


import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.merp.android.R
import kotlinx.android.synthetic.main.fragment_edit_entry.*

/**
 * The Kotlin class for the fragment used in [EditEarningActivity] and [EditExpenseActivity].
 */
class EditEntryFragment : Fragment() {

    /**
     * Inflates the layout for this fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_entry, container, false)
    }
}