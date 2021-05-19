package com.tvmedicine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
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
    /**Метод для запроса через Корутину*/
   private fun request() {
        val mService = Common.retrofitService
        val sPref = getSharedPreferences("User", MODE_PRIVATE)
        val userType: String? = sPref.getString("user_type", "")
        if(userType=="patient")
        {
       val call = mService.getTreatmentByUser("getTreatment.php",sPref.getString("phone_number",""))
            println(sPref.getString("phone_number",""))
            val result = call?.execute()?.body()
            val call2 = mService.getPatientFromId("getPatient.php", result?.get(0)?.patient_id)
            val call3 = mService.getDoctorFromId("getDoctor.php", result?.get(0)?.doctor_id)
            val result2 = call2?.execute()?.body()
            val result3 = call3?.execute()?.body()
            data.add(result2?.get(0)?.surename)
            data.add(result3?.get(0)?.surename)
            data.add(result?.get(0)?.start_date)
        }


    }
    private fun request2(result: List<TreatmentModel?>?) {
        val mService = Common.retrofitService
            val call2 = mService.getPatientFromId("getPatient.php", result?.get(0)?.patient_id)
            val call3 = mService.getDoctorFromId("getDoctor.php", result?.get(0)?.doctor_id)
            val result2 = call2?.execute()?.body()
            val result3 = call3?.execute()?.body()
            data.add(result2?.get(0)?.surename)
            data.add(result3?.get(0)?.surename)
            data.add(result?.get(0)?.start_date)
        }


    }
    private fun request3() {
        val mService = Common.retrofitService
        val sPref = getSharedPreferences("User", MODE_PRIVATE)
        val userType: String? = sPref.getString("user_type", "")
        if(userType=="patient")
        {
            val call = mService.getTreatmentByUser("getTreatment.php",sPref.getString("phone_number",""))
            println(sPref.getString("phone_number",""))
            val result = call?.execute()?.body()
            val call2 = mService.getPatientFromId("getPatient.php", result?.get(0)?.patient_id)
            val call3 = mService.getDoctorFromId("getDoctor.php", result?.get(0)?.doctor_id)
            val result2 = call2?.execute()?.body()
            val result3 = call3?.execute()?.body()
            data.add(result2?.get(0)?.surename)
            data.add(result3?.get(0)?.surename)
            data.add(result?.get(0)?.start_date)
        }


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
