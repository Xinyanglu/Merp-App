package com.merp.android

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

/**
 * Constructs an instance of the CustomAdapter class, a modified version of the default ArrayAdapter class.
 * Uses the same properties as an ArrayAdapter class.
 */
class CustomAdapter(context: Context,
                    resource: Int,
                    items: List<CustomListItem>)
            : ArrayAdapter<CustomListItem>(context, resource, items){

    /** TODO: ??? */
    private val mContext = context

    /** The layout resource of the ListView */
    private val mResource = resource

    /** The list of [CustomListItem]s to be adapted to a ListView */
    private val mItems = items

    /**
     * Constructs a ViewHolder object for holding four TextViews:
     * [textViewDate], [textViewSource], [textViewAmount], [textViewAddInfo].
     * Each TextView is used to display one of four [CustomListItem] properties.
     */
    private class ViewHolder {
        /** TextView for displaying [CustomListItem.tvDate] */
        var textViewDate: TextView? = null

        /** TextView for displaying [CustomListItem.tvSource] */
        var textViewSource: TextView? = null

        /** TextView for displaying [CustomListItem.tvAmount] */
        var textViewAmount: TextView? = null

        /** TextView for displaying [CustomListItem.tvAddInfo] */
        var textViewAddInfo: TextView? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var v = convertView
        val holder: ViewHolder

        if(v == null){
            holder = ViewHolder()
            v = LayoutInflater.from(mContext).inflate(mResource, null)
            v.tag = holder
        }else{
            holder = v.tag as ViewHolder
        }

        val p = getItem(position)

        if(p != null) {
            holder.textViewDate = v?.findViewById(R.id.textDate)
            holder.textViewSource = v?.findViewById(R.id.textSource)
            holder.textViewAmount = v?.findViewById(R.id.textAmount)
            holder.textViewAddInfo = v?.findViewById(R.id.textAddInfo)

            if(holder.textViewDate != null) {
                holder.textViewDate!!.text = mItems[position].getTVDate()
            }
            if(holder.textViewSource != null) {
                holder.textViewSource?.text = mItems[position].getTVSource()
            }
            if(holder.textViewAmount != null) {
                holder.textViewAmount?.text = mItems[position].getTVAmount()
            }
            if(holder.textViewAddInfo != null) {
                holder.textViewAddInfo?.text = mItems[position].getTVAddInfo()
            }
        }
        return v!!
    }
}