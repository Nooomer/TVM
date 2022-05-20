package com.tvmedicine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tvmedicine.RetrifitService.Common
import com.tvmedicine.models.MessagesModel
import kotlinx.coroutines.*

class AttachActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    var result: List<MessagesModel?>? = null
    var data_string = Array<String>(10){""}
    private var viewSize: Int = 0
    val scope = CoroutineScope(Dispatchers.Main + Job())
    val data: Array<Array<String?>> = Array(10) { Array(3) { "" } }
    private fun <T> CoroutineScope.asyncIO(ioFun: () -> T) = async(Dispatchers.IO) { ioFun() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attach)
        recyclerView = findViewById(R.id.rv_view3)
        recyclerView.layoutManager = LinearLayoutManager(this)
        load(scope, result, recyclerView,0)
    }
    private fun allMessageRequest(): List<MessagesModel?>? {
        val mService = Common.retrofitService
        val sPref = getSharedPreferences("User", MODE_PRIVATE)
        val call = mService.getMessages("getChatMessage.php", 1)
        //viewSize = result!!.size
        result = call?.execute()?.body()
        return result
    }
    private fun load(scope: CoroutineScope, result: List<MessagesModel?>?, recyclerView: RecyclerView, itemCount: Int?){
        var result1 = result
        scope.launch {
            val def = scope.asyncIO { result1 = allMessageRequest() }
            def.await()
            viewSize = result1!!.size
            println(viewSize)
            println(result1)
            if (itemCount != null) {
                for (i in itemCount until viewSize) {
                   // messageDate = result1?.get(i)?.message_datetime.toString()
                    if(result1!![i]?.sound_server_link!=null)
                    {
                        data[i][1] = result1?.get(i)?.message_datetime
                        data_string[i] = result1!![i]?.message_id.toString()
                    }
                }
                recyclerView.adapter = soundRvAdapter(data)
            }
        }
    }
}