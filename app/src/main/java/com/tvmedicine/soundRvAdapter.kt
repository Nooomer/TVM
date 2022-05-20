package com.tvmedicine

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**Class for [RecyclerView.Adapter]*/
class soundRvAdapter(private var soundList: Array<Array<String?>>) :
    RecyclerView.Adapter<soundRvAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var buttonViewLarge: View? = null
        var textViewLarge: TextView? = null

        init {
            buttonViewLarge = itemView.findViewById(R.id.play_button)
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
        holder.textViewLarge?.text = soundList[position][1]

    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

}