package com.merp.android

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

/**
 * A custom [ArrayAdapter] class, modified for adapting the four properties of each [CustomListItem] into one item in a ListView.
 *
 * @constructor Constructs an instance of the CustomAdapter class using the same properties as an [ArrayAdapter] object.
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

    /**
     * TODO called repeatedly for each item in the ListView??
     *
     * @param [position] position of the item in the ListView
     * @param [convertView] TODO
     * @param [parent] TODO the parent ViewGroup of the View??? wat unused
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var v = convertView
        val holder: ViewHolder

        //set up the ViewHolder which will hold the four TextViews
        if(v == null){
            holder = ViewHolder()
            v = LayoutInflater.from(mContext).inflate(mResource, null)
            v.tag = holder
        }else{
            holder = v.tag as ViewHolder
        }

        val p = getItem(position)

        //if an item in the list exists at the position, set up the four TextViews for that item
        if(p != null) {
            holder.textViewDate = v?.findViewById(R.id.textDate)
            holder.textViewSource = v?.findViewById(R.id.textSource)
            holder.textViewAmount = v?.findViewById(R.id.textAmount)
            holder.textViewAddInfo = v?.findViewById(R.id.textAddInfo)

            //set the text for textViewDate
            if(holder.textViewDate != null) {
                /*
                ensure that the date visually appears as the left as opposed to the right

                            2020-                   2020-0
                            01-01                   1-01
                 */
                val date = mItems[position].getTVDate().replaceFirst("-", "-\n")
                holder.textViewDate!!.text = date
            }

            //set the text for textViewSource
            if(holder.textViewSource != null) {
                holder.textViewSource?.text = mItems[position].getTVSource()
            }

            //set the text for textViewAmount
            if(holder.textViewAmount != null) {
                holder.textViewAmount?.text = mItems[position].getTVAmount()
            }

            //set the text for textViewAddInfo
            if(holder.textViewAddInfo != null) {
                //if the additional information exceeds 10 characters, display only the first
                //10 characters followed by "..." to save space
                var addInfo = mItems[position].getTVAddInfo()
                if(addInfo.length > 10){
                    addInfo = addInfo.substring(0, 10) + "..."
                }
                holder.textViewAddInfo?.text = addInfo
            }
        }
        return v!!
    }
}