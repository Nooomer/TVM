package com.tvmedicine

import android.content.Context
import android.media.MediaPlayer
import java.io.File

class AppVoicePlayer(private val context: Context) {

    private lateinit var mMediaPlayer: MediaPlayer
    private lateinit var mFile: File
    private var recordController = UploadRecord()

    fun play(messageId: String,chatId: String, fileUrl: String, function: () -> Unit) {
        mFile = File("${context.cacheDir.absolutePath}${chatId}-${messageId+1}.wav")
        if (mFile.exists() && mFile.length() > 0 && mFile.isFile) {
            startPlay {
                function()
            }
        } else {
            mFile.createNewFile()
            mFile = File("${context.cacheDir.absolutePath}${recordController.download()}.wav")
        }
    }



    private fun startPlay(function: () -> Unit) {
        try {
            mMediaPlayer.setDataSource(mFile.absolutePath)
            mMediaPlayer.prepare()
            mMediaPlayer.start()
            mMediaPlayer.setOnCompletionListener {
                stop {
                    function()
                }
            }
        } catch (e: Exception) {
        }
    }

    fun stop(function: () -> Unit) {
        try {
            mMediaPlayer.stop()
            mMediaPlayer.reset()
            function()
        } catch (e: Exception) {
            function()
        }
    }

    fun release() {
        mMediaPlayer.release()
    }

    fun init(){
        mMediaPlayer = MediaPlayer()
    }
}