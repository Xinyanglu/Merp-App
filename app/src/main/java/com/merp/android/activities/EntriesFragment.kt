package com.merp.android.activities

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView

import com.merp.android.R

class EntriesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_entries, container, false)
        val metrics = context!!.resources.displayMetrics
        val width = metrics.widthPixels
        val list = v.findViewById<ListView>(R.id.listEntries)
        Log.d("DEVICEWIDTH", width.toString())
        list.minimumWidth = width
        return v
    }
}
