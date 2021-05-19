package com.tvmedicine


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.progressindicator.BaseProgressIndicator
import com.google.android.material.progressindicator.LinearProgressIndicator
import kotlinx.coroutines.*

class TreatmentActivity : AppCompatActivity() {
    val data = mutableListOf<String?>()
    var viewSize: Int = 0
    private fun <T> CoroutineScope.asyncIO(ioFun: () -> T) = async(Dispatchers.IO) { ioFun() }
    /**Метод для запроса через Корутину*/
   private fun patientRequest(): List<TreatmentModel?> {
        val mService = Common.retrofitService
        val sPref = getSharedPreferences("User", MODE_PRIVATE)
       val call = mService.getTreatmentByUser("getTreatment.php",sPref.getString("login",""))
            println(sPref.getString("login",""))
        val result = call?.execute()?.body()
        viewSize = result!!.size
        data.add(result[0]?.start_date)
        return result
        }
    private fun getPatientName(result: List<TreatmentModel?>?) {
        val mService = Common.retrofitService
            val call = mService.getPatientFromId("getPatient.php", result?.get(0)?.patient_id)
            val result2 = call?.execute()?.body()
            data.add(result2?.get(0)?.surename)
        }
    private fun getDoctorName(result: List<TreatmentModel?>?) {
        val mService = Common.retrofitService
        val call = mService.getPatientFromId("getDoctor.php", result?.get(0)?.doctor_id)
        val result3 = call?.execute()?.body()
        data.add(result3?.get(0)?.surename)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        val arguments = intent.extras
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treatment)
        val indicator = findViewById<LinearProgressIndicator>(R.id.ProgressIndicator)
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
        var result: List<TreatmentModel?>? = null
        scope.launch {
            val deferredList = listOf(
                    scope.asyncIO { result = patientRequest() }
            )
            deferredList.awaitAll()
        }
        scope.launch {
            val deferredList2 = listOf(
                    scope.asyncIO { getPatientName(result) },
                    scope.asyncIO { getDoctorName(result) }
            )
            deferredList2.awaitAll()
            indicator.hide()
            recyclerView.adapter = rv_adapter(data as List<String>, viewSize)
        }
        for(i in 1..viewSize){
            scope.launch {
                val deferredList = listOf(
                        scope.asyncIO { result = patientRequest() }
                )
                deferredList.awaitAll()
            }
            scope.launch {
                val deferredList2 = listOf(
                        scope.asyncIO { getPatientName(result) },
                        scope.asyncIO { getDoctorName(result) }
                )
                deferredList2.awaitAll()
                indicator.hide()
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }
        val fab1 = findViewById<FloatingActionButton>(R.id.out_btn)
        fab1.setOnClickListener {
            val sPref = getSharedPreferences("User", MODE_PRIVATE)
            val ed: SharedPreferences.Editor = sPref.edit()
            ed.clear()
            ed.apply()
            val intent = Intent(
                    applicationContext,
                    MainActivity::class.java
            )
            startActivity(intent)
        }
    }
}

