package com.tvmedicine.models
/**Patient response model */
data class PatientModel(
        val id : Int,
        val surename : String,
        val name : String,
        val sName : String,
        val phoneNumber : String,
        val insuranceNumber : String,
        val password : String
        )
