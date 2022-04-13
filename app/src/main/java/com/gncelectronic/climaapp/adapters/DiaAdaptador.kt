package com.gncelectronic.climaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.gncelectronic.climaapp.R
import com.gncelectronic.climaapp.modelo.Dia
import kotlinx.android.synthetic.main.semanal_item.view.*

class DiaAdaptador(val context: Context, val dataSource : ArrayList<Dia>):BaseAdapter() {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder : ViewHolder
        val view : View
        if(convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.semanal_item,parent,false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        }else{
            viewHolder = convertView.tag as ViewHolder
            view = convertView
        }

        val convertView = dataSource[position]
        viewHolder.apply {
            dayTextView.text=convertView.getFormattedTime()
            minTextView.text="Min. ${convertView.minTemp.toInt()} °C"
            maxTextView.text="Max. ${convertView.maxTemp.toInt()} °C"

        }
        return view
    }

    override fun getItem(position: Int): Any {
       return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
      return 0
    }

    override fun getCount(): Int {
      return dataSource.size
    }


    private class ViewHolder(view:View){
        val dayTextView : TextView = view.dayTextView
        val minTextView : TextView = view.minTextView
        val maxTextView : TextView = view.maxTextView
    }
}