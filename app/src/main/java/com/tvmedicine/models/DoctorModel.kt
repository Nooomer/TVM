package com.tvmedicine.models
/**Response model for doctor information
 * @param id it id's from DB. Return type [Int]
 * @param surename it doctor surename from DB. Return type [String]
 * @param name it doctor name from DB. Return type [String]
 * @param sName it doctor middle name from DB. Return type [String]
 * @param phoneNumber it doctor phone_number from DB. Return type [String]
 * @param password it doctor password from DB. Return type [String]
 * @param hospitalId it doctor hospital id's from DB. Return type [Int]*/
data class DoctorModel(
        val id : Int,
        val surename : String,
        val name : String,
        val sName : String,
        val phoneNumber : String,
        val password : String,
        val hospitalId : Int
        )
