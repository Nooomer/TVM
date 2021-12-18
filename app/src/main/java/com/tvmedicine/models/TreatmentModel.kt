package com.tvmedicine.models

data class TreatmentModel(
        val id : Int,
        val chatId: Int,
        val patientId : Int,
        val doctorId : Int,
        val startDate: String,
        val symptomsId: Int,
        val soundServerLinkId: Int,
        val conclusionId:Int
)
