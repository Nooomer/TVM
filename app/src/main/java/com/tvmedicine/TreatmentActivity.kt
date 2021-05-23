package com.tvmedicine


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
    val data: Array<Array<String?>> = Array(10) { Array(3) { "" } }
    private var viewSize: Int = 0
    var startDate: String? = ""
    var patientSurename: String? = ""
    var doctorSurename: String? = ""
    private fun <T> CoroutineScope.asyncIO(ioFun: () -> T) = async(Dispatchers.IO) { ioFun() }
    /**Метод для запроса через Корутину*/
   private fun patientRequest(): List<TreatmentModel?> {
        val mService = Common.retrofitService
        val sPref = getSharedPreferences("User", MODE_PRIVATE)
       val call = mService.getTreatmentByUser("getTreatment.php",sPref.getString("login",""))
            println(sPref.getString("login",""))
        val result = call?.execute()?.body()
        viewSize = result!!.size
        startDate = result[0]?.start_date
        return result
        }
    private fun getPatientName(result: List<TreatmentModel?>?) {
        val mService = Common.retrofitService
        val call = mService.getPatientFromId("getPatient.php", result?.get(0)?.patient_id)
        val result2 = call?.execute()?.body()
        patientSurename = result2?.get(0)?.surename
    }
    private fun getDoctorName(result: List<TreatmentModel?>?) {
        val mService = Common.retrofitService
        val call = mService.getPatientFromId("getDoctor.php", result?.get(0)?.doctor_id)
        val result3 = call?.execute()?.body()
        doctorSurename = result3?.get(0)?.surename
    }
    private fun doctorRequest(): List<TreatmentModel?> {
        val mService = Common.retrofitService
        val call = mService.getAllTreatment("getTreatment.php")
        val result = call?.execute()?.body()
        viewSize = result!!.size
        return result
    }
    private fun getPatientNameForDoctor(result: List<TreatmentModel?>?, i:Int) {
        val mService = Common.retrofitService
        val call = mService.getPatientFromId("getPatient.php", result?.get(i)?.patient_id)
        val result2 = call?.execute()?.body()
        startDate = result?.get(i)?.start_date
        patientSurename = result2?.get(0)?.surename
    }
    private fun getDoctorNameForDoctor(result: List<TreatmentModel?>?,i:Int) {
        val mService = Common.retrofitService
        val call = mService.getPatientFromId("getDoctor.php", result?.get(i)?.doctor_id)
        val result3 = call?.execute()?.body()
        doctorSurename = result3?.get(0)?.surename
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        val arguments = intent.extras
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treatment)
        val sPref = getSharedPreferences("User", MODE_PRIVATE)
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
        if(sPref.getString("user_type","")=="patient") {
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
                data[0][0] = patientSurename
                data[0][1] = doctorSurename
                data[0][2] = startDate
                indicator.hide()
                recyclerView.adapter = rvAdapter(data, viewSize)
            }

            for (i in 1..viewSize) {
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
                    data[i][0] = patientSurename
                    data[i][1] = doctorSurename
                    data[i][2] = startDate
                    indicator.hide()
                    recyclerView.adapter = rvAdapter(data, viewSize)
                    recyclerView.adapter?.notifyDataSetChanged()
                }
            }
        }
        if(sPref.getString("user_type","")=="doctor") {
            scope.launch {
                val deferredList = listOf(
                        scope.asyncIO { result = doctorRequest() }
                )
                deferredList.awaitAll()
            }
            for (i in 0..viewSize) {
                scope.launch {
                    val deferredList2 = listOf(
                            scope.asyncIO { getPatientNameForDoctor(result,i) },
                            scope.asyncIO { getDoctorNameForDoctor(result,i) }
                    )
                    deferredList2.awaitAll()
                    data[i][0] = patientSurename
                    data[i][1] = doctorSurename
                    data[i][2] = startDate

                    recyclerView.adapter = rvAdapter(data, viewSize)
                    println(viewSize)
                    recyclerView.adapter?.notifyDataSetChanged()
                }

            }
            indicator.hide()
        }
        val fab1 = findViewById<FloatingActionButton>(R.id.out_btn)
        fab1.setOnClickListener {
            val sPref = getSharedPreferences("User", MODE_PRIVATE)
            val ed: SharedPreferences.Editor = sPref.edit()
            ed.clear()
            ed.apply()
            /*val intent = Intent(
                  /applicationContext,
                    //MainActivity::class.java
            //)
            //startActivity(intent)*/
            finish()
        }
    }
}

