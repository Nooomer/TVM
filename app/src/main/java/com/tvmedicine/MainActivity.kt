package com.tvmedicine

import android.R.id.message
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun doctor_auth_btn(view: View){
        val li: LayoutInflater = LayoutInflater.from(this);
        val alertView: View = li.inflate(R.layout.alert, null);

        //Создаем AlertDialog
       val mDialogBuilder: AlertDialog.Builder  = AlertDialog.Builder(this);

        //Настраиваем alert.xml для нашего AlertDialog:
        mDialogBuilder.setView(alertView);

        //Настраиваем отображение поля для ввода текста в открытом диалоге:
        val userInput1: EditText  = alertView.findViewById(R.id.phone_number_field);
        val userInput2: EditText  = alertView.findViewById(R.id.password_field);
        val api: API = API()
        mDialogBuilder
                .setCancelable(true)
                .setPositiveButton(getString(R.string.login_btn)) { _: DialogInterface, _: Int ->
                    val ret = api.doctorAuth(userInput1.text.toString(),userInput2.text.toString())
                    if(ret){
                        val toast = Toast.makeText(applicationContext, getString(R.string.auth_result_good), Toast.LENGTH_SHORT)
                        toast.show()
                    }
                    else{
                        val toast = Toast.makeText(applicationContext, getString(R.string.auth_result_bad), Toast.LENGTH_SHORT)
                        toast.show()
                    }
                }
                .setNegativeButton(getString(R.string.cancel_btn)) { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.cancel()
                }
        val alertDialog: AlertDialog = mDialogBuilder.create();
        alertDialog.show();

    }
    fun click(view: View){
        val intent = Intent(this@MainActivity, debug::class.java)
        startActivity(intent)
    }
}