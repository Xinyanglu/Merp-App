package com.merp.android.activities


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout

import com.merp.android.R
import kotlinx.android.synthetic.main.fragment_help.*
import kotlinx.android.synthetic.main.fragment_help.view.*

class HelpFragment : Fragment() {
    internal lateinit var callback: OnCloseClickedListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val helpLayout = inflater.inflate(R.layout.fragment_help, container, false).findViewById<FrameLayout>(R.id.helpLayout)
        //val closeButton = inflater.inflate(R.layout.fragment_help, container, false).findViewById<Button>(R.id.btnClose)
        helpLayout.btnClose.setOnClickListener{
            callback.onCloseClicked()
        }
            //TODO: how to get fragment to commit sudoku?

            //activity?.fragmentManager?.beginTransaction()?.remove(this )?.commit()



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_help, container, false)
    }

    interface OnCloseClickedListener{
        fun onCloseClicked()
    }

    fun setOnCloseClickedListener(callback: OnCloseClickedListener){
        this.callback = callback
    }
}