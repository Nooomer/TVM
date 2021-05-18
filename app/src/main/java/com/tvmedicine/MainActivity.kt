package com.tvmedicine

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    /**Method for hiding the keyboard*/
    private fun hideKeyboardFrom(context: Context, view: View) {
        val imm: InputMethodManager =
            context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    /**A method that implements the logic of patient authorization*/
    fun patientAuthBtn(view: View){
        val li: LayoutInflater = LayoutInflater.from(this)
        val alertView: View = li.inflate(R.layout.alert, null)
        val loadingView: View = li.inflate(R.layout.loading, null)
        //Создаем AlertDialog
        val mDialogBuilder: AlertDialog.Builder  = AlertDialog.Builder(this)
        val mDialogBuilder2: AlertDialog.Builder = AlertDialog.Builder(this)
        //Настраиваем alert.xml для нашего AlertDialog:
        mDialogBuilder.setView(alertView)
        mDialogBuilder2.setView(loadingView)
        //Настраиваем отображение поля для ввода текста в открытом диалоге:
        val userInput1: EditText  = alertView.findViewById(R.id.phone_number_field)
        val userInput2: EditText  = alertView.findViewById(R.id.password_field)
        //Билдер для диалога авторизации
        mDialogBuilder2
                .setCancelable(false)
        val alertDialog2: AlertDialog = mDialogBuilder2.create()
        //Диалог для входа
        mDialogBuilder
                .setCancelable(true)
                .setPositiveButton(getString(R.string.login_btn)) { _: DialogInterface, _: Int ->
                    hideKeyboardFrom(applicationContext,alertView)
                    alertDialog2.show()
                    //Авторизация для пациента
                    val mService2 = Common.retrofitService
                    mService2.auth("authUser.php",userInput1.text.toString(), userInput2.text.toString())
                            ?.enqueue(object : Callback<List<AuthModel?>?> {
                                override fun onResponse(
                                    call: Call<List<AuthModel?>?>?,
                                    AuthResponse: Response<List<AuthModel?>?>?
                                ) {
                                    if (AuthResponse?.body()?.get(0)?.response == "true") {
                                        val intent = Intent(
                                                applicationContext,
                                                TreatmentActivity::class.java
                                        )
                                        intent.putExtra("user_type", "patient");
                                        alertDialog2.cancel()
                                        startActivity(intent)
                                    }
                                    else{
                                        val toast = Toast.makeText(
                                                applicationContext,
                                                getString(R.string.auth_complete),
                                                Toast.LENGTH_SHORT
                                        )
                                        alertDialog2.cancel()
                                        toast.show()
                                    }
                                }
                                override fun onFailure(call: Call<List<AuthModel?>?>?, t: Throwable?) {
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
        val alertDialog: AlertDialog = mDialogBuilder.create()
        alertDialog.show()
    }
   /**A method that implements the logic of doctor authorization*/
    fun doctorAuthBtn(view: View){
        val li: LayoutInflater = LayoutInflater.from(this)
        val alertView: View = li.inflate(R.layout.alert, null)
        val loadingView: View = li.inflate(R.layout.loading, null)
        //Создаем AlertDialog
       val mDialogBuilder: AlertDialog.Builder  = AlertDialog.Builder(this)
        val mDialogBuilder2: AlertDialog.Builder = AlertDialog.Builder(this)
        //Настраиваем alert.xml для нашего AlertDialog:
        mDialogBuilder.setView(alertView)
        mDialogBuilder2.setView(loadingView)
        //Настраиваем отображение поля для ввода текста в открытом диалоге:
        val userInput1: EditText  = alertView.findViewById(R.id.phone_number_field)
        val userInput2: EditText  = alertView.findViewById(R.id.password_field)
        //Билдер для диалога авторизации
        mDialogBuilder2
            .setCancelable(false)
        val alertDialog2: AlertDialog = mDialogBuilder2.create()
        //Диалог для входа
        mDialogBuilder
                .setCancelable(true)
                .setPositiveButton(getString(R.string.login_btn)) { _: DialogInterface, _: Int ->
                    hideKeyboardFrom(applicationContext,alertView)
                    alertDialog2.show()
                    //Авторизация для доктора
                    val mService = Common.retrofitService
                    mService.auth("doctorAuth.php",userInput1.text.toString(), userInput2.text.toString())
                        ?.enqueue(object : Callback<List<AuthModel?>?> {
                            override fun onResponse(
                                    call: Call<List<AuthModel?>?>?,
                                    response: Response<List<AuthModel?>?>?
                            ) {
                                if (response?.body()?.get(0)?.response == "true") {
                                    val intent = Intent(
                                        applicationContext,
                                        DoctorActivity::class.java
                                    )
                                    intent.putExtra("user_type", "doctor");
                                    alertDialog2.cancel()
                                    startActivity(intent)
                                }
                                else{
                                    val toast = Toast.makeText(
                                            applicationContext,
                                            getString(R.string.auth_complete),
                                            Toast.LENGTH_SHORT
                                    )
                                    alertDialog2.cancel()
                                    toast.show()
                                }
                            }
                            override fun onFailure(call: Call<List<AuthModel?>?>?, t: Throwable?) {
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
        val alertDialog: AlertDialog = mDialogBuilder.create()
        alertDialog.show()
    }

}