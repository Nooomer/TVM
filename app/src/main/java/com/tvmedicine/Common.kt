package com.tvmedicine

object Common {
    private val BASE_URL = "https://1515714571567515.000webhostapp.com"
    val retrofitService: RetrofitServices
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitServices::class.java)
}