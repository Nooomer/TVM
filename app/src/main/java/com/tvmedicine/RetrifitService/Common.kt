package com.tvmedicine.RetrifitService

import com.tvmedicine.API

object Common {
    private const val BASE_URL = "https://1515714571567515.000webhostapp.com"
    val retrofitService: API
        get() = RetrofitClient.getClient(BASE_URL).create(API::class.java)
}