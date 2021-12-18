package com.tvmedicine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_activity)
        val recyclerView = findViewById<RecyclerView>(R.id.rv_view2)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}