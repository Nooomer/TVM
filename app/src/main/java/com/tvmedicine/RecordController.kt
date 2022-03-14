package com.tvmedicine
import android.content.Context
import android.media.MediaRecorder
import android.util.Log
import com.tvmedicine.models.MessagesModel
import com.tvmedicine.UploadRecord

class RecordController(private val context: Context) {
    private var upload = UploadRecord()
    private var audioRecorder: MediaRecorder? = null
    fun start(result: List<MessagesModel?>?) {
        Log.d(TAG, "Start")
        audioRecorder = MediaRecorder().apply{
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(getAudioPath(result?.get(result.lastIndex)!!.chat_id,
                result[result.lastIndex]!!.message_id))
            prepare()
            start()
        }
        upload.savePath(getAudioPath(result?.get(result.lastIndex)!!.chat_id,
            result[result.lastIndex]!!.message_id),"${result[result.lastIndex]!!.chat_id}+${result[result.lastIndex]!!.message_id}")
    }
    private fun getAudioPath(chatId: Int, messageId: Int): String {
        return "${context.cacheDir.absolutePath}${chatId}-${messageId}.wav"
    }

    fun stop() {
        audioRecorder?.let {
            Log.d(TAG, "Stop")
            it.stop()
            it.release()
        }
        audioRecorder = null
    }
    fun isAudioRecording() = audioRecorder != null
    fun getVolume() = audioRecorder?.maxAmplitude ?: 0
    private companion object {
        private val TAG = RecordController::class.java.name
    }
}