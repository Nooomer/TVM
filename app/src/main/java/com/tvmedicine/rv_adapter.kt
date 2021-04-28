package com.tvmedicine

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class rv_adapter(private name: String, s_name:String,donable: String) :
    RecyclerView.Adapter<rv_adapter.MyViewHolder>()  {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var largeTextView: TextView? = null
        var largeTextView2: TextView? = null
        var smallTextView: TextView? = null

        init {
            largeTextView = itemView.findViewById(R.id.textViewLarge)
            largeTextView2 = itemView.findViewById(R.id.textViewLarge2)
            smallTextView = itemView.findViewById(R.id.textViewSmall)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.largeTextView?.text = name
        holder.largeTextView2?.text = s_name
        holder.smallTextView?.text = donable
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}