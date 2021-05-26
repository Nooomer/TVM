package com.tvmedicine



import android.content.ContextWrapper
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.progressindicator.BaseProgressIndicator
import com.google.android.material.progressindicator.LinearProgressIndicator
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*


class TreatmentActivity : AppCompatActivity() {
    val data: Array<Array<String?>> = Array(10) { Array(3) { "" } }
    val symptomsArray: Array<Array<String?>> = Array(6) { Array(2) { "" } }
    private var viewSize: Int = 0
    var startDate: String? = ""
    var patientSurename: String? = ""
    var doctorSurename: String? = ""
    var spinner: Spinner? = null
   lateinit var sPref: SharedPreferences
   lateinit var indicator: LinearProgressIndicator
   lateinit var recyclerView: RecyclerView
    val scope = CoroutineScope(Dispatchers.Main + Job())
    val result: List<TreatmentModel?>? = null
    private fun <T> CoroutineScope.asyncIO(ioFun: () -> T) = async(Dispatchers.IO) { ioFun() }

    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    /**Метод для запроса через Корутину*/
    private fun patientRequest(): List<TreatmentModel?>? {
        val mService = Common.retrofitService
        val sPref = getSharedPreferences("User", MODE_PRIVATE)
        val call = mService.getTreatmentByUser("getTreatment.php", sPref.getString("login", ""))
        println(sPref.getString("login", ""))
        val result = call?.execute()?.body()
        //viewSize = result!!.size
        return result
    }

    private fun treatmentAdding(symptoms_id: Int, sound_server_link_id: Int): Boolean {
        val mService = Common.retrofitService
        val sPref = getSharedPreferences("User", MODE_PRIVATE)
        val call = mService.addTreatment("addTreatment.php", sPref.getString("login", ""), Calendar.getInstance().time.toString("yyyy/MM/dd HH:mm:ss"), symptoms_id, sound_server_link_id)
        val result = call?.execute()?.body()
        return result?.get(0)?.response != "false"
    }

    private fun symptomsRequest(): ArrayAdapter<String> {
        val mService = Common.retrofitService
        val call = mService.getAllSymptoms("getSymptoms.php")
        val result = call?.execute()?.body()
        val symptoms: Array<String?> = arrayOf(result?.get(0)?.symptoms_name, result?.get(1)?.symptoms_name, result?.get(2)?.symptoms_name, result?.get(3)?.symptoms_name, result?.get(4)?.symptoms_name, result?.get(5)?.symptoms_name)
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, symptoms)
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        return adapter
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

    private fun doctorRequest(): List<TreatmentModel?>? {
        val mService = Common.retrofitService
        val call = mService.getAllTreatment("getTreatment.php")
        val result = call?.execute()?.body()
        //viewSize = result!!.size
        return result
    }

    private fun getPatientNameForDoctor(patientId: Int?) {
        val mService = Common.retrofitService
        val call = mService.getPatientFromId("getPatient.php", patientId)
        val result2 = call?.execute()?.body()
        patientSurename = result2?.get(0)?.surename
    }

    private fun getDoctorNameForDoctor(doctorId: Int?) {
        if(doctorId == 0){
            return
        }
        val mService = Common.retrofitService
        val call = mService.getDoctorFromId("getDoctor.php", doctorId)
        val result3 = call?.execute()?.body()
        doctorSurename = result3?.get(0)?.surename
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val arguments = intent.extras
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treatment)
        sPref = getSharedPreferences("User", MODE_PRIVATE)
        indicator = findViewById<LinearProgressIndicator>(R.id.ProgressIndicator)
        indicator.showAnimationBehavior = BaseProgressIndicator.SHOW_OUTWARD
        indicator.hideAnimationBehavior = BaseProgressIndicator.HIDE_OUTWARD
        indicator.show()
        recyclerView = findViewById(R.id.rv_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val li: LayoutInflater = LayoutInflater.from(this)
        val alertView: View = li.inflate(R.layout.alert, null)
        val loadingView: View = li.inflate(R.layout.loading, null)
        val addView: View = li.inflate(R.layout.add_layout, null)
        //Создаем AlertDialog
        val mDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        val mDialogBuilder2: AlertDialog.Builder = AlertDialog.Builder(this)
        val mDialogBuilder3: AlertDialog.Builder = AlertDialog.Builder(this)

        val spinner = addView.findViewById<View>(R.id.symptoms_spinner) as Spinner

        //Настраиваем alert.xml для нашего AlertDialog:
        mDialogBuilder.setView(alertView)
        mDialogBuilder2.setView(loadingView)
        mDialogBuilder3.setView(addView)

        load(sPref, scope, result, recyclerView, indicator)
        val fab1 = findViewById<FloatingActionButton>(R.id.out_btn)
        fab1.setOnClickListener {
            val sPref = getSharedPreferences("User", MODE_PRIVATE)
            val ed: SharedPreferences.Editor = sPref.edit()
            ed.clear()
            ed.apply()
            finish()
        }
        val fab2 = findViewById<FloatingActionButton>(R.id.add_btn)
        if (sPref.getString("user_type", "") == "doctor") {
            fab2.visibility = View.GONE
        }
        fab2.setOnClickListener {
            var adapter: ArrayAdapter<String>? = null
            scope.launch {
                val def = scope.asyncIO { adapter = symptomsRequest() }
                def.await()
                spinner.adapter = adapter
            }
            mDialogBuilder3
                    .setCancelable(false)
                    .setPositiveButton("Добавить обращение") { _: DialogInterface, _: Int ->
                        scope.launch {

                            val def = scope.asyncIO { treatmentAdding(spinner.selectedItemPosition + 1, sound_server_link_id = 1) }
                            def.await()
                            load(sPref, scope, result, recyclerView, indicator)
                        }

                    }
                    .setNegativeButton("Отмена") { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.cancel()
                    }

            mDialogBuilder3.create()
            mDialogBuilder3.show()

        }
    }

    private fun load(sPref: SharedPreferences, scope: CoroutineScope, result: List<TreatmentModel?>?, recyclerView: RecyclerView, indicator: LinearProgressIndicator) {
        var result1 = result
        indicator.show()
        if (sPref.getString("user_type", "") == "patient") {
            scope.launch {
                val def = scope.asyncIO { result1 = patientRequest() }
                def.await()
                viewSize = result1!!.size


                println(viewSize)
                for (i in 0 until viewSize) {
                    val def1 = scope.asyncIO { getPatientNameForDoctor(result1?.get(i)?.patient_id) }
                    def1.await()
                    val def2 = scope.asyncIO { getDoctorNameForDoctor(result1?.get(i)?.doctor_id) }
                    def2.await()
                    startDate = result1?.get(i)?.start_date
                    if(doctorSurename==""){
                        doctorSurename = "Врач не назначен"
                    }
                    data[i][0] = patientSurename
                    data[i][1] = doctorSurename
                    data[i][2] = startDate
                    recyclerView.adapter = rvAdapter(data, viewSize)
                    println(i)
                    recyclerView.adapter?.notifyDataSetChanged()
                    patientSurename = ""
                    doctorSurename = ""
                    startDate = ""
                }
                indicator.hide()
            }

        }
        if (sPref.getString("user_type", "") == "doctor") {
            scope.launch {
                val def = scope.asyncIO { result1 = patientRequest() }
                def.await()
                viewSize = result1!!.size


                println(viewSize)
                for (i in 0 until viewSize) {
                    val def1 = scope.asyncIO { getPatientNameForDoctor(result1?.get(i)?.patient_id) }
                    def1.await()
                    val def2 = scope.asyncIO { getDoctorNameForDoctor(result1?.get(i)?.doctor_id) }
                    def2.await()
                    startDate = result1?.get(i)?.start_date
                    data[i][0] = patientSurename
                    data[i][1] = doctorSurename
                    data[i][2] = startDate
                    recyclerView.adapter = rvAdapter(data, viewSize)
                    println(i)
                    recyclerView.adapter?.notifyDataSetChanged()
                }
                indicator.hide()
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Job().cancel()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.app_bar_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh_button -> load(sPref, scope, result, recyclerView, indicator)
        }
        return true
    }

}


