package com.tvmedicine

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface RetrofitServices {
    @GET("api/{method}")
    fun doctorAuth(
            @Path("method") method_name: String?,
        @Query("phone_number") phone_number: String?,
        @Query("password") password: String
    ): Call<List<authModel?>?>?
    fun userAuth(
            @Path("method") method_name: String?,
            @Query("snils") phone_number: String?,
            @Query("password") password: String
    ): Call<List<authModel?>?>?
    fun getPatient(
            @Path("method") method_name: String?
    ): Call<List<getPatientModel?>?>?
}