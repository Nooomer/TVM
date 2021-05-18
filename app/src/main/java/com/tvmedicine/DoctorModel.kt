package com.tvmedicine
/**Response model for doctor information*/
data class DoctorModel(
        val id : Int,
        val surename : String,
        val name : String,
        val s_name : String,
        val phone_number : String,
        val password : String,
        val hospital_id : Int
        )
