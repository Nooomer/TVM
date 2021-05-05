package com.tvmedicine

data class PatientModel(
        val id : Int,
        val surename : String,
        val name : String,
        val s_name : String,
        val text_complaints : String,
        val sound_server_link_id : Int?,
        val phone_number : String,
        val password : String
        )
