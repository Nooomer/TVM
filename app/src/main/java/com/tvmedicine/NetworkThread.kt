package com.tvmedicine

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import com.tvmedicine.NetworkThread

interface NetworkThread {
    @GET("doctorAuth.php")
    fun getData(
        @Query("number") resourceName: String?,
        @Query("password") count: String
    ): Call<List<NetworkThread?>?>?
}