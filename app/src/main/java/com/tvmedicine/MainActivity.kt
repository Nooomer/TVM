package com.tvmedicine

import android.R.id.message
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun doctor_auth_btn(view: View){
        val input: EditText = EditText(this)
        AlertDialog.Builder(this)
                .setTitle("Update Status")
                .setMessage("message")
                .setView(input)
                .setPositiveButton("Ok") { dialog, whichButton -> val value: Editable = input.text }.setNegativeButton("Cancel") { dialog, whichButton ->
                    // Do nothing.
                }.show()
    }
    fun click(view: View){
        val intent = Intent(this@MainActivity, debug::class.java)
        startActivity(intent)
    }
}