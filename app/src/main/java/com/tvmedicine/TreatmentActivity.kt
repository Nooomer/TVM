package com.tvmedicine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TreatmentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treatment)
        val recyclerView: RecyclerView = findViewById(R.id.rv_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val li: LayoutInflater = LayoutInflater.from(this)
        val alertView: View = li.inflate(R.layout.alert, null)
        val loading_view: View = li.inflate(R.layout.loading, null)
        //Создаем AlertDialog
        val mDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        val mDialogBuilder2: AlertDialog.Builder = AlertDialog.Builder(this)
        //Настраиваем alert.xml для нашего AlertDialog:
        mDialogBuilder.setView(alertView)
        mDialogBuilder2.setView(loading_view)
        //Настраиваем отображение поля для ввода текста в открытом диалоге:
        val userInput1: EditText = alertView.findViewById(R.id.phone_number_field)
        val userInput2: EditText = alertView.findViewById(R.id.password_field)
        val data = mutableListOf<String?>()
        var doctor_surname: String?
        var patient_surname: String?
        var size: Int = 0
        //Билдер для загрузки
       // mDialogBuilder2
               //.setCancelable(false)
        //val alertDialog2: AlertDialog = mDialogBuilder2.create()
//alertDialog2.show()

        //Диалог для входа

                    //Получение всех пациентов
                    val mService = Common.retrofitService
                    mService.getAllTreatment("getTreatment.php")
                            ?.enqueue(object : Callback<List<TreatmentModel?>?> {
                                override fun onResponse(
                                        call: Call<List<TreatmentModel?>?>?,
                                        TreatmentResponse: Response<List<TreatmentModel?>?>?
                                ) {
                                    (0 until TreatmentResponse?.body()?.size!!).forEach { i ->
                                    mService.getDoctorFromId("getDoctor.php", TreatmentResponse.body()?.get(i)!!.doctor_id)
                                            ?.enqueue(object : Callback<List<DoctorModel?>?> {
                                                override fun onResponse(call: Call<List<DoctorModel?>?>, response: Response<List<DoctorModel?>?>) {

                                                        doctor_surname = response.body()!![i]!!.surename
                                                        data.add(doctor_surname)

                                                }
                                                override fun onFailure(call: Call<List<DoctorModel?>?>, t: Throwable) {
                                                    val toast = Toast.makeText(
                                                            applicationContext,
                                                            t.toString(),
                                                            Toast.LENGTH_SHORT
                                                    )
                                                    toast.show()
                                                }
                                            })
                                        mService.getPatientFromId("getPatient.php", TreatmentResponse.body()?.get(i)!!.patient_id)
                                                ?.enqueue(object : Callback<List<PatientModel?>?> {
                                                    override fun onResponse(call: Call<List<PatientModel?>?>, response: Response<List<PatientModel?>?>) {
                                                           patient_surname = response.body()!![i]!!.surename
                                                            data.add(patient_surname)
                                                    }
                                                    override fun onFailure(call: Call<List<PatientModel?>?>, t: Throwable) {
                                                        val toast = Toast.makeText(
                                                                applicationContext,
                                                                t.toString(),
                                                                Toast.LENGTH_SHORT
                                                        )
                                                        toast.show()
                                                    }})


                                        data.add(TreatmentResponse.body()!![i]?.status)
                                        size = TreatmentResponse.body()!!.size
                                        recyclerView.adapter = rv_adapter(data as List<String>, size)
                                }

                                }
                                override fun onFailure(call: Call<List<TreatmentModel?>?>, t: Throwable) {
                                    val toast = Toast.makeText(
                                            applicationContext,
                                            t.toString(),
                                            Toast.LENGTH_SHORT
                                    )
                                    toast.show()
                                }
                            })

//alertDialog2.cancel()
    }
}