package com.tvmedicine

import android.content.Context
import android.media.MediaPlayer
import java.io.File

class AppVoicePlayer(private val context: Context) {

    private lateinit var mMediaPlayer: MediaPlayer
    private lateinit var mFile: File
    private var recordController = UploadRecord()

    fun play(messageId: String,chatId: String) {
        mFile = File("${context.cacheDir.absolutePath}${chatId}-${messageId+1}.wav")
        if (mFile.exists() && mFile.length() > 0 && mFile.isFile) {
            startPlay()
        } else {
            mFile.createNewFile()
            mFile = File("${context.cacheDir.absolutePath}${recordController.download()}.wav")
        }
    }



    private fun startPlay() {
        try {
            mMediaPlayer.setDataSource(mFile.absolutePath)
            mMediaPlayer.prepare()
            mMediaPlayer.start()
            mMediaPlayer.setOnCompletionListener {
                stop()
            }
        } catch (e: Exception) {
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