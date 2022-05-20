package com.tvmedicine

import android.content.Context
import android.media.MediaPlayer
import kotlinx.coroutines.*
import java.io.File

class AppVoicePlayer(private val context: Context) {

    private lateinit var mMediaPlayer: MediaPlayer
    private lateinit var mFile: File
    private var recordController = UploadRecord()
    private fun <T> CoroutineScope.asyncIO(ioFun: () -> T) = async(Dispatchers.IO) { ioFun() }
    fun play(messageId: String,chatId: String) {
        mFile = File("${context.cacheDir.absolutePath}${chatId}-${messageId.toInt()}.wav")
        println(mFile.absoluteFile)
        println(mFile.exists())
        println(mFile.length())
        println(mFile.isFile)
        if (mFile.exists() && mFile.length() > 0 && mFile.isFile) {
            println("start play")
            startPlay()
        } else {
            println("download")
            mFile.createNewFile()
            val scope = CoroutineScope(Dispatchers.Main + Job())
            scope.launch {
                val def = scope.asyncIO {
                    recordController.download(
                        chatId,
                        messageId,
                        context
                    )
                }
                def.await()
                println("downloaded")
                mFile = File(
                    "${context.cacheDir.absolutePath}${chatId}-${messageId.toInt()}.wav"
                )
                startPlay()
                println("started")
            }
            }
        }

    private fun startPlay() {
        init()
        println(mFile.absolutePath)
            mMediaPlayer.setDataSource(mFile.absolutePath)
            mMediaPlayer.prepare()
            mMediaPlayer.start()
            println("playing")
            mMediaPlayer.setOnCompletionListener {
                stop()
                release()
            }
    }

    fun stop() {
            mMediaPlayer.stop()
            mMediaPlayer.reset()
        release()
    }

    private fun release() {
        mMediaPlayer.release()
    }

    private fun init(){
        mMediaPlayer = MediaPlayer()
    }
}