package com.merp.android.activities


import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.merp.android.R
import kotlinx.android.synthetic.main.fragment_edit_entry.*

class EditEntryFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_edit_entry, container, false)
        val layout = v.findViewById<FrameLayout>(R.id.editEntryFragmentLayout)
        //layout.foreground.alpha = 0 TODO(): add foreground back
        return v
    }

    fun dimForeground(dim: Boolean) {
        //I have no idea what I'm doing here c:
        val viewGroup: ViewGroup? = view?.rootView?.findViewById(android.R.id.content)
        LayoutInflater.from(context).inflate(R.layout.fragment_edit_entry, viewGroup, false)

        if (dim) editEntryFragmentLayout.foreground.alpha = 200 //dim
        else editEntryFragmentLayout.foreground.alpha = 0 //un-dim
    }
}