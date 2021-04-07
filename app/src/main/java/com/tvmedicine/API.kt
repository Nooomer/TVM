package com.tvmedicine


import androidx.lifecycle.LiveData
import com.beust.klaxon.Klaxon
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
    fun doctorAuth(number: String, password:String): Boolean{
        val lgc: Boolean
        val url = "https://1515714571567515.000webhostapp.com/doctorAuth.php?phone_number=$number&password=$password"
                    val doc: Document = Jsoup
                        .connect(url)
                        .get()
                    val BodyText = doc.select("body")
                   lgc = BodyText.text().toBoolean()
        return lgc
    }
}