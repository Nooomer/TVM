package com.tvmedicine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.BaseProgressIndicator
import com.google.android.material.progressindicator.LinearProgressIndicator
import kotlinx.coroutines.*

class TreatmentActivity : AppCompatActivity() {
    val data = mutableListOf<String?>()
    private fun <T> CoroutineScope.asyncIO(ioFun: () -> T) = async(Dispatchers.IO) { ioFun() }
    /**Метод для запроса через Корутину */
   private fun request() {
        val mService = Common.retrofitService
       val call = mService.getAllTreatment("getTreatment.php")
        val result = call?.execute()?.body()
       data.add(result?.get(0)?.patient_id.toString())
        data.add(result?.get(0)?.patient_id.toString())
        data.add(result?.get(0)?.patient_id.toString())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        val arguments = intent.extras
        val UserType = arguments!!["user_type"].toString()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treatment)
        var indicator = findViewById<LinearProgressIndicator>(R.id.ProgressIndicator)
        indicator.showAnimationBehavior = BaseProgressIndicator.SHOW_OUTWARD
        indicator.hideAnimationBehavior = BaseProgressIndicator.HIDE_OUTWARD
        indicator.show()
        val recyclerView: RecyclerView = findViewById(R.id.rv_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val li: LayoutInflater = LayoutInflater.from(this)
        val alertView: View = li.inflate(R.layout.alert, null)
        val loadingView: View = li.inflate(R.layout.loading, null)
        //Создаем AlertDialog
        val mDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        val mDialogBuilder2: AlertDialog.Builder = AlertDialog.Builder(this)
        //Настраиваем alert.xml для нашего AlertDialog:
        mDialogBuilder.setView(alertView)
        mDialogBuilder2.setView(loadingView)
        val job = Job()
        val scope = CoroutineScope(Dispatchers.Main + job)
        scope.launch {
            val deferredList = listOf(
                    scope.asyncIO { request() }
            )
            deferredList.awaitAll()
            indicator.hide()
            recyclerView.adapter = rv_adapter(data as List<String>, data.size)
        }
    }
}
