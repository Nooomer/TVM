package com.tvmedicine

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import com.beust.klaxon.Klaxon
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


class debug : AppCompatActivity() {
    private val api: API = API()
    var txt: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug)
//        val methods_list:Array<String> = arrayOf("getUsers")
//        val spinner = findViewById<View>(R.id.spinner) as Spinner
//        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
//        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
//        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, methods_list)
//        // Определяем разметку для использования при выборе элемента
//        // Определяем разметку для использования при выборе элемента
//        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
//        // Применяем адаптер к элементу spinner
//        // Применяем адаптер к элементу spinner
//        spinner.adapter = adapter
        txt = findViewById(R.id.textView2)
    }
   data class patients(
            val id: Int,
            val surename: String,
            val name: String,
            val s_name: String,
            val text_complaints: String,
            val sound_server_link: String,
            val doktor_id: Int,
            val diagnosed: String,
    )
    fun get(view :View){
        /*val user: LiveData<patients> = liveData {
            try {
                val doc: Document = Jsoup
                    .connect(api.getUsers())
                    .get()
                val result = Klaxon()
                    .parse<patients>(doc.toString())

                txt?.text = result?.name + result?.s_name
            }
            catch(ioException: Exception) {

            }

        }*/
    }
}