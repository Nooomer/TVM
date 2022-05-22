package com.tvmedicine

import RecyclerItemClickListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tvmedicine.RetrifitService.Common
import com.tvmedicine.models.MessagesModel
import kotlinx.coroutines.*

class AttachActivity : AppCompatActivity() {
    private var position: Int = 0
    private var player: AppVoicePlayer = AppVoicePlayer(this)
    lateinit var recyclerView: RecyclerView
    var result: List<MessagesModel?>? = null
    var lastPlayPosition = 0
    var audio_message_id = mutableListOf<Int>()
    var audio_message_datetime = mutableListOf<String?>()
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
        val recyclerView = findViewById<RecyclerView>(R.id.rv_view3)
        var playing = false
        recyclerView.addOnItemTouchListener(
            RecyclerItemClickListener(
                this,
                recyclerView,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        this@AttachActivity.position = position
                        playing = if (!playing) {
                            player.play(audio_message_id[position].toString(),"1")
                            lastPlayPosition = position
                            true
                        } else {
                            if ((lastPlayPosition != position) and (playing)) {
                                player.stop()
                                player.play(audio_message_id[position].toString(), "1")
                                lastPlayPosition = position
                                true
                            } else {
                                if ((lastPlayPosition == position)and(!playing)) {
                                    player.play(audio_message_id[position].toString(), "1")
                                    lastPlayPosition = position
                                    true
                                }
                                else{
                                    if ((lastPlayPosition == position)and(playing)) {
                                        player.play(audio_message_id[position].toString(), "1")
                                        lastPlayPosition = position
                                        true
                                    }
                                    else{
                                        player.stop()
                                        false
                                    }

                                }
                                }
                        }
                        }
//


                    override fun onLongItemClick(view: View?, position: Int) {
                        Toast.makeText(this@AttachActivity,"Голосовое сообщение", Toast.LENGTH_LONG).show()
                    }
                })
        )
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
                        audio_message_datetime.add(result1?.get(i)?.message_datetime)
                        //data_string[i] = result1!![i]?.message_id.toString()
                        audio_message_id.add(i+1)
                    }
                }
                recyclerView.adapter = soundRvAdapter(audio_message_datetime, audio_message_id.size)
            }
        }
    }
}