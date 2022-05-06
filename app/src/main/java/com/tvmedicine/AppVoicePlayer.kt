package com.tvmedicine

import android.content.Context
import android.media.MediaPlayer
import java.io.Console
import java.io.File

class AppVoicePlayer(private val context: Context) {

    private var mMediaPlayer: MediaPlayer = MediaPlayer()
    private lateinit var mFile: File
    private var recordController = UploadRecord()

    fun play(messageId: String,chatId: String) {
        mFile = File("${context.cacheDir.absolutePath}${chatId}-${messageId.toInt()}.wav")
        println(mFile.absoluteFile)
        println(mFile.absolutePath)
        if (mFile.exists() && mFile.length() > 0 && mFile.isFile) {
            println("start play")
            startPlay()
        } else {
            println("download")
            mFile.createNewFile()
            mFile = File("${context.cacheDir.absolutePath}${recordController.download()}.wav")
        }
    }



    private fun startPlay() {
            mMediaPlayer.setDataSource(mFile.absolutePath)
            mMediaPlayer.prepare()
            mMediaPlayer.start()
            println("playing")
            mMediaPlayer.setOnCompletionListener {
                stop()
            }
    }

    fun stop() {
        try {
            mMediaPlayer.stop()
            mMediaPlayer.reset()
        } catch (e: Exception) {
        }
    }

    fun release() {
        mMediaPlayer.release()
    }

    fun init(){
        mMediaPlayer = MediaPlayer()
    }
}