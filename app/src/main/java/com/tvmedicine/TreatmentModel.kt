package com.tvmedicine

data class TreatmentModel(
        val id : Int,
        val patient_id : Int,
        val doctor_id : Int,
        val status : String
)
