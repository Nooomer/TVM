package com.tvmedicine

import android.content.DialogInterface
import android.content.Intent
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

class DoctorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor)
        val recyclerView: RecyclerView = findViewById(R.id.rv_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        lateinit var mService: RetrofitServices
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


        //Билдер для диалога авторизации
        mDialogBuilder2
            .setCancelable(false)
        val alertDialog2: AlertDialog = mDialogBuilder2.create()


        //Диалог для входа
        /* mDialogBuilder
                .setCancelable(true)
                .setPositiveButton(getString(R.string.login_btn)) { _: DialogInterface, _: Int ->
                    alertDialog2.show()
                    var ret: Boolean = false


                    //Получение всех пациентов
                    val mService = Common.retrofitService
                    mService.getPatient("getPatient.php")
                            ?.enqueue(object : Callback<List<PatientModel?>?> {


                                override fun onResponse(
                                        call: Call<List<PatientModel?>?>?,
                                        response: Response<List<PatientModel?>?>?
                                ) {

                                        val intent = Intent(
                                                applicationContext,
                                                DoctorActivity::class.java
                                        )
                                        alertDialog2.cancel()
                                        startActivity(intent)

                                }

                                override fun onFailure(call: Call<List<PatientModel?>?>?, t: Throwable?) {
                                    val toast = Toast.makeText(
                                            applicationContext,
                                            t.toString(),
                                            Toast.LENGTH_SHORT
                                    )
                                    alertDialog2.cancel()
                                    toast.show()
                                }
                            })

                }
                .setNegativeButton(getString(R.string.cancel_btn)) { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.cancel()
                }
        val alertDialog: AlertDialog = mDialogBuilder.create();
        alertDialog.show();
        recyclerView.adapter = rvAdapter(fillList())
    }*/

        fun fillList(): List<String> {
            val data = mutableListOf<String>()
            (0..30).forEach { i -> data.add("$i element") }
            return data
        }
    }
}