package com.tvmedicine

import android.graphics.Color
import kotlinx.coroutines.*
import org.apache.commons.net.ftp.FTPClient
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class UploadRecord {
    private var path:String = ""
    var name:String = ""
    fun savePath(path: String, name: String){
        this.path = path
        this.name = name
    }
    private fun <T> CoroutineScope.asyncIO(ioFun: () -> T) = async(Dispatchers.IO) { ioFun() }
    @Throws(FileNotFoundException::class)
    fun upload() {
        val scope = CoroutineScope(Dispatchers.Main + Job())
        val fClient = FTPClient()
        val fInput = FileInputStream(path)
        val fs = "/www/u1554079.isp.regruhosting.ru/audio/$name.wav"
        scope.launch {
            val def = scope.asyncIO { val hostAddress = "31.31.196.105"
                fClient.connect(hostAddress)
                fClient.enterLocalPassiveMode()
                val log = "u1554079"
                val password = "y8P22m5yJwPGS8Yw"
                fClient.login(log, password)
                fClient.storeFile(fs, fInput)
                fClient.logout()
                fClient.disconnect() }
            def.await()
            }
        }
    @Throws(FileNotFoundException::class)
    fun download() {
        val scope = CoroutineScope(Dispatchers.Main + Job())
        val fClient = FTPClient()
        val fInput = FileOutputStream(path)
        val fs = "/www/u1554079.isp.regruhosting.ru/audio/$name.wav"
        scope.launch {
            val def = scope.asyncIO { val hostAddress = "31.31.196.105"
                fClient.connect(hostAddress)
                fClient.enterLocalPassiveMode()
                val log = "u1554079"
                val password = "y8P22m5yJwPGS8Yw"
                fClient.login(log, password)
                fClient.retrieveFile(fs, fInput)
                fClient.logout()
                fClient.disconnect() }
            def.await()
        }
    }

    }