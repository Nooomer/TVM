package com.tvmedicine

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface RetrofitServices {
    @GET("api/{method}")
    fun auth(
            @Path("method") method_name: String?,
            @Query("phone_number") phone_number: String?,
            @Query("password") password: String
    ): Call<List<authModel?>?>?
    @GET("api/{method}")
    fun getAllPatient(
            @Path("method") method_name: String?
    ): Call<List<PatientModel?>?>?
    @GET("api/{method}")
    fun getPatientFromId(
            @Path("method") method_name: String?,
            @Query("id") id:Int
    ): Call<List<PatientModel?>?>?
    @GET("api/{method}")
    fun getAllTreatment(
            @Path("method") method_name: String?,
    ): Call<List<TreatmentModel?>?>?
    @GET("api/{method}")
    fun getTreatmentFromId(
            @Path("method") method_name: String?,
            @Query("id") id:Int
    ): Call<List<TreatmentModel?>?>?
    @GET("api/{method}")
    fun getAllDoctor(
            @Path("method") method_name: String?,
    ): Call<List<DoctorModel?>?>?
    @GET("api/{method}")
    fun getDoctorFromId(
            @Path("method") method_name: String?,
            @Query("id") id:Int
    ): Call<List<DoctorModel?>?>?
}