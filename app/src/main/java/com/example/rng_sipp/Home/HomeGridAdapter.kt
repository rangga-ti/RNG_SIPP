package com.example.rng_sipp.Home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.rng_sipp.R

class HomeGridAdapter(
    private val context: Context,
    private val menuList: List<QuickMenuItem>
) : BaseAdapter() {

    private class ViewHolder(view: View) {
        val ivIcon: ImageView = view.findViewById(R.id.ivGridIcon)
        val tvTitle: TextView = view.findViewById(R.id.tvGridTitle)
    }

    override fun getCount(): Int = menuList.size

    override fun getItem(position: Int): Any = menuList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_grid_menu, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        val item = menuList[position]
        holder.ivIcon.setImageResource(item.iconRes)
        holder.tvTitle.text = item.title

        return view
    }
}