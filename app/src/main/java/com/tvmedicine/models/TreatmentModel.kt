package com.tvmedicine.models

data class TreatmentModel(
        val id : Int,
        val patient_id : Int,
        val doctor_id : Int,
        val start_date: String,
        val symptoms_id: Int,
        val sound_server_link_id: Int,
        val conclusion_id:Int
)
