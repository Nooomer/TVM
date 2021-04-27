package com.tvmedicine

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface RetrofitServices {
    @GET("doctorAuth.php")
    fun getData(
        @Query("number") resourceName: String?,
        @Query("password") count: String
    ): Call<List<authModel?>?>?
}