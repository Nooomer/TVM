package com.tvmedicine

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore.Audio
import android.util.Log
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.tvmedicine.RetrifitService.Common
import com.tvmedicine.models.AuthModel
import com.tvmedicine.models.MessagesModel
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min


class ChatActivity : AppCompatActivity() {
    lateinit var sPref: SharedPreferences
    var data = mutableListOf<MessageItemUi>()
    var data_string = Array<String>(10) { "" }
    var numOfMessageWithSound = mutableListOf<Int?>()
    private var viewSize: Int = 0
    private var messageDate = ""
    lateinit var recyclerView: RecyclerView
    val scope = CoroutineScope(Dispatchers.Main + Job())
    var result: List<MessagesModel?>? = null
    var result2: List<AuthModel?>? = null
    private fun <T> CoroutineScope.asyncIO(ioFun: () -> T) = async(Dispatchers.IO) { ioFun() }
    private var player: AppVoicePlayer = AppVoicePlayer(this)
    private lateinit var playButton: View
    private lateinit var audioButton: View
    private lateinit var attachButton: Button
    private val recordController = RecordController(this)
    private var countDownTimer: CountDownTimer? = null
    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.RECORD_AUDIO),
            777,
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_activity)
        sPref = getSharedPreferences("User", MODE_PRIVATE)
        println("here")
        recyclerView = findViewById(R.id.rv_view2)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val itemCount: Int?
        itemCount = 0
        val sendMessageButton = findViewById<FloatingActionButton>(R.id.send_message_button)
        sPref = getSharedPreferences("User", MODE_PRIVATE)
       attachButton = findViewById<Button>(R.id.attach_button).apply {
           setOnClickListener {
               val intent = Intent(
                   applicationContext,
                   ChatActivity::class.java
               )
               intent.putExtra("audio_num", data_string)
               startActivity(intent) }
       }
        audioButton = findViewById<View>(R.id.start_button).apply {
            setOnClickListener { onSendMessageButtonClicked() }
        }
        var playing = false
        playButton = findViewById<View>(R.id.play_button).apply {
            setOnClickListener {
                playing = if (!playing) {
                    player.play("12","1")
                    true
                } else {
                    player.stop()
                    false
                }
            }
        }
        sendMessageButton.setOnClickListener {
            scope.launch {
                val def = scope.asyncIO { result2 = sendMessage() }
                def.await()
                viewSize = result2!!.size
                println(viewSize)
                println(result2)
                when(result2!![0]?.response){
                    "true" -> {
                        load(scope, result, recyclerView, recyclerView.adapter?.itemCount)
                    }
                    "false" -> {
                        val toast = Toast.makeText(
                            applicationContext,
                            "Что-то пошло не так, сообщение не отправилось",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }
                    else -> {
                        val toast = Toast.makeText(
                            applicationContext,
                            "Все еще хуже,все совсем сломалось",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }
                }

            }
        }
        load(scope, result, recyclerView,itemCount)
    }
    private fun onSendMessageButtonClicked() {
        if (recordController.isAudioRecording()) {
            recordController.stop()
            countDownTimer?.cancel()
            countDownTimer = null
            scope.launch {
                val def = scope.asyncIO { result2 = sendMessage() }
                def.await()
                viewSize = result2!!.size
                println(viewSize)
                println(result2)
                when(result2!![0]?.response){
                    "true" -> {
                        load(scope, result, recyclerView, recyclerView.adapter?.itemCount)
                    }
                    "false" -> {
                        val toast = Toast.makeText(
                            applicationContext,
                            "Что-то пошло не так, сообщение не отправилось",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }
                    else -> {
                        val toast = Toast.makeText(
                            applicationContext,
                            "Все еще хуже,все совсем сломалось",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }
                }

            }
        } else {
            recordController.start(result)
            countDownTimer = object : CountDownTimer(60_000, VOLUME_UPDATE_DURATION) {
                override fun onTick(p0: Long) {
                    val volume = recordController.getVolume()
                    Log.d(TAG, "Volume = $volume")
                    handleVolume(volume)
                }

                override fun onFinish() {

                }
            }.apply {
                start()
            }
        }
    }
    private fun handleVolume(volume: Int) {
        val scale = min(8.0, volume / MAX_RECORD_AMPLITUDE + 1.0).toFloat()
        Log.d(TAG, "Scale = $scale")

        audioButton.animate()
            .scaleX(scale)
            .scaleY(scale)
            .setInterpolator(interpolator)
            .duration= VOLUME_UPDATE_DURATION
    }
    private fun allMessageRequest(): List<MessagesModel?>? {
        val mService = Common.retrofitService
        val sPref = getSharedPreferences("User", MODE_PRIVATE)
        val call = mService.getMessages("getChatMessage.php", 1)
        //viewSize = result!!.size
        result = call?.execute()?.body()
        return result
    }
    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
    private fun sendMessage(): List<AuthModel?>? {
        val messageText = findViewById<TextView>(R.id.message_text)
        val mService = Common.retrofitService
        val sPref = getSharedPreferences("User", MODE_PRIVATE)
        val call = mService.sendMessages("sendMessage.php",1,messageText.text.toString(),recordController.getLastUploadPath(),getCurrentDateTime().toString("yyyy/MM/dd HH:mm:ss"),sPref.getString("user_type", ""))
        result2 = call?.execute()?.body()
        return result2
    }
    private fun String?.toUserType():Int {
        when(this){
            "patient" -> {
                return when(sPref.getString("user_type", "")){
                    "patient" ->{
                        0
                    }
                    "doctor" -> {
                        1
                    }
                    else -> {
                        -1
                    }
                }

            }
            "doctor" ->{
                return when(sPref.getString("user_type", "")){
                    "patient" ->{
                        1
                    }
                    "doctor" -> {
                        0
                    }
                    else -> {
                        -1
                    }
                }
            }
            else ->{
                return -1
            }
        }
    }

    private fun load(scope: CoroutineScope, result: List<MessagesModel?>?, recyclerView: RecyclerView, itemCount: Int?){
        numOfMessageWithSound.clear()
        var result1 = result
            scope.launch {
                val def = scope.asyncIO { result1 = allMessageRequest() }
                def.await()
                viewSize = result1!!.size
                println(viewSize)
                println(result1)
                if (itemCount != null) {
                    for (i in itemCount until viewSize) {
                        messageDate = result1?.get(i)?.message_date_time.toString()
                        if(result1!![i]?.sound_server_link!=null)
                        {
                        data.add(i,MessageItemUi("${result1!![i]?.text}\n${result1!![i]?.sound_server_link}",Color.WHITE,result1!![i]?.user_type.toUserType()))
                            numOfMessageWithSound.add(result1!![i]?.message_id)
                            data_string[i] = result1!![i]?.message_id.toString()
                            }
                        else{
                            data.add(i,MessageItemUi("${result1!![i]?.text}",Color.WHITE,result1!![i]?.user_type.toUserType()))
                        }
                        recyclerView.adapter = ChatAdapter(data)
                    }
                }
            }
    }
    private companion object {
        private val TAG = MainActivity::class.java.name
        private const val MAX_RECORD_AMPLITUDE = 32768.0
        private const val VOLUME_UPDATE_DURATION = 100L
        private val interpolator = OvershootInterpolator()
    }
}