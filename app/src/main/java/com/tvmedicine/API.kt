package com.tvmedicine



import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import com.beust.klaxon.Klaxon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

data class result(
        val result_line: String
)
class API {
    fun getUsers():String{
        return "https://1515714571567515.000webhostapp.com/getUsers.php"
    }
     fun doctorAuth(number: String, password:String): Boolean {
         var lgc: Boolean = true
         val ntwrk = NetworkThread()
         CoroutineScope(Dispatchers.IO).launch {
             lgc = ntwrk.frst(number, password)
         }
         return lgc
     }
    }