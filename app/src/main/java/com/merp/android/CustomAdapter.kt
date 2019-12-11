package com.merp.android

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class CustomAdapter(context: Context,
                    resource: Int,
                    items: List<CustomListItem>)
            : ArrayAdapter<CustomListItem>(context, resource, items){

    private val mContext = context
    private val resourceLayout = resource
    private val mItems = items

    private class ViewHolder {
        var textView1: TextView? = null
        var textView2: TextView? = null
        var textView3: TextView? = null
        var textView4: TextView? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var v = convertView
        val holder: ViewHolder

        if(v == null){
            holder = ViewHolder()
            v = LayoutInflater.from(mContext).inflate(resourceLayout, null)
            v.tag = holder
        }else{
            holder = v.tag as ViewHolder
        }

        val p = getItem(position)

        if(p != null) {
            holder.textView1 = v?.findViewById(R.id.textDate)
            holder.textView2 = v?.findViewById(R.id.textSource)
            holder.textView3 = v?.findViewById(R.id.textAmount)
            holder.textView4 = v?.findViewById(R.id.textAddInfo)

            if(holder.textView1 != null) {
                holder.textView1!!.text = mItems[position].getTVDate()
            }
            if(holder.textView2 != null) {
                holder.textView2?.text = mItems[position].getTVSource()
            }
            if(holder.textView3 != null) {
                holder.textView3?.text = mItems[position].getTVAmount()
            }
            if(holder.textView4 != null) {
                holder.textView4?.text = mItems[position].getTVAddInfo()
            }
        }
        return v!!
    }
}