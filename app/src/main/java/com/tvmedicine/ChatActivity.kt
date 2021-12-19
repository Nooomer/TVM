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
    var result: List<MessagesModel?>? = null
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
    private fun <T> CoroutineScope.asyncIO(ioFun: () -> T) = async(Dispatchers.IO) { ioFun() }
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
        val call = mService.getMessages("getChatMessage.php", 1)
        //viewSize = result!!.size
        result = call?.execute()?.body()
        return result
    }
    private fun String?.toUserType():Int {
        when(this){
            "patient" -> {
                when(sPref.getString("user_type", "")){
                    "patient" ->{
                        return  0
                    }
                    "doctor" -> {
                        return  1
                    }
                    else -> {
                        return  -1
                    }
                }

            }
            "doctor" ->{
                when(sPref.getString("user_type", "")){
                    "patient" ->{
                        return  1
                    }
                    "doctor" -> {
                        return  0
                    }
                    else -> {
                        return  -1
                    }
                }
            }
            else ->{
                return -1
            }
        }
    }
    private fun load(sPref: SharedPreferences, scope: CoroutineScope, result: List<MessagesModel?>?, recyclerView: RecyclerView){
        var result1 = result
        if (sPref.getString("user_type", "") == "patient") {
            scope.launch {
                val def = scope.asyncIO { result1 = allMessageRequest() }
                def.await()
                viewSize = result1!!.size
                println(viewSize)
                println(result1)
                for (i in 0 until viewSize) {
                    messageDate = result1?.get(i)?.message_date_time.toString()
                    data.add(i,MessageItemUi(result1!![i]?.text,Color.WHITE,result1!![i]?.user_type.toUserType()))
                    recyclerView.adapter = ChatAdapter(data)
                }
            }
        }
        if (sPref.getString("user_type", "") == "doctor") {
            scope.launch {
                val def = scope.asyncIO { result1 = allMessageRequest() }
                def.await()
                viewSize = result1!!.size
                println(viewSize)
                println(result1)
                for (i in 0 until viewSize) {
                    messageDate = result1?.get(i)?.message_date_time.toString()
                    data.add(i,MessageItemUi(result1!![i]?.text,Color.WHITE,result1!![i]?.user_type.toUserType()))
                    recyclerView.adapter = ChatAdapter(data)
                }
            }
        }
    }

}