package com.tvmedicine

import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.tvmedicine.RetrifitService.Common
import com.tvmedicine.models.MessagesModel
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class ChatActivity : AppCompatActivity() {
    lateinit var sPref: SharedPreferences

    val data = mutableListOf<MessageItemUi>()
    private var viewSize: Int = 0
    private var messageDate = ""
    lateinit var indicator: LinearProgressIndicator
    lateinit var recyclerView: RecyclerView
    val scope = CoroutineScope(Dispatchers.Main + Job())
    val result: List<MessagesModel?>? = null
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
    private fun <T> CoroutineScope.asyncIO(ioFun: () -> T) = async(dispatcher) { ioFun() }
    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_activity)
        sPref = getSharedPreferences("User", MODE_PRIVATE)
        recyclerView = findViewById(R.id.rv_view2)
        recyclerView.layoutManager = LinearLayoutManager(this)
        load(sPref, scope, result, recyclerView)

    }
    private fun allMessageRequest(): List<MessagesModel?>? {
        val mService = Common.retrofitService
        val sPref = getSharedPreferences("User", MODE_PRIVATE)
        val call = mService.getMessages("getAllMessage.php", 1)
        //viewSize = result!!.size
        return call?.execute()?.body()
    }
    private fun String?.toUserType():Int {
        if (this == "patient") {
            return 0
        } else {
            if (this == "doctor") {
                return 1
            }
        }
        return -1
    }
    private fun load(sPref: SharedPreferences, scope: CoroutineScope, result: List<MessagesModel?>?, recyclerView: RecyclerView){
        var result1 = result
        if (sPref.getString("user_type", "") == "patient") {
            scope.launch {
                val def = scope.asyncIO { result1 = allMessageRequest() }
                def.await()
                viewSize = result1!!.size
                println(viewSize)
                for (i in 0 until viewSize) {
                    messageDate = result1?.get(i)?.messageDateTime.toString()
                    data.add(i,MessageItemUi(result1!![i]?.text,Color.WHITE,result1!![i]?.text.toUserType()))
                    recyclerView.adapter = ChatAdapter(data)
                }
            }
        }
    }

}