package com.tvmedicine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class NetworkThread() {
     suspend fun frst(number: String, password:String):Boolean = withContext(Dispatchers.IO){
        val lgc: Boolean
        val url = "https://1515714571567515.000webhostapp.com/doctorAuth.php?phone_number=$number&password=$password"
        val doc: Document = Jsoup
            .connect(url)
            .get()
        val BodyText = doc.select("body")
        lgc = BodyText.text().toBoolean()
        return@withContext true
    }
}
