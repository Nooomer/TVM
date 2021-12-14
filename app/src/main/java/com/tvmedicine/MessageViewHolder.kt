package com.tvmedicine

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

abstract class MessageViewHolder<in T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: T)
}

class MyMessageViewHolder(val view: View) : MessageViewHolder<MessageItemUi>(view) {
    private val messageContent = view.findViewById<TextView>(R.id.message)

    override fun bind(item: MessageItemUi) {
        messageContent.text = item.content
        messageContent.setTextColor(item.textColor)
    }


}
class FriendMessageViewHolder(val view: View) : MessageViewHolder<MessageItemUi>(view) {
    private val messageContent = view.findViewById<TextView>(R.id.message)

    override fun bind(item: MessageItemUi) {
        messageContent.text = item.content
        messageContent.setTextColor(item.textColor)
    }


}