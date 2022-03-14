package com.tvmedicine

import org.apache.commons.net.ftp.FTPClient
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

class UploadRecord {
    private var path:String = ""
    var name:String = ""
    fun savePath(path: String, name: String){
        this.path = path
        this.name = name
    }
    @Throws(FileNotFoundException::class)
    fun upload() {
        val fClient = FTPClient()
        val fInput = FileInputStream(path)
        val fs = name
        try {
            val hostAddress = "31.31.196.105"
            fClient.connect(hostAddress)
            fClient.enterLocalPassiveMode()
            val log = "u1554079"
            val password = "y8P22m5yJwPGS8Yw"
            fClient.login(log, password)
            fClient.storeFile(fs, fInput)
            fClient.logout()
            fClient.disconnect()
        } catch (ex: IOException) {
            System.err.println(ex)
        }
    }
}