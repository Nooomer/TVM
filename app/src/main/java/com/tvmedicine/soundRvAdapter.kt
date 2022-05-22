package com.tvmedicine

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**Class for [RecyclerView.Adapter]*/
class soundRvAdapter(private var soundList: MutableList<String?>, private val itemCount: Int) :
    RecyclerView.Adapter<soundRvAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var buttonViewLarge: View? = null
        var textViewLarge: TextView? = null

        init {
            textViewLarge = itemView.findViewById(R.id.textViewLarge)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.sound_rv_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textViewLarge?.text = soundList[position]

    }

    override fun getItemCount(): Int {
        return itemCount
    }

}