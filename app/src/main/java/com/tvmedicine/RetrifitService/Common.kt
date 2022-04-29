package com.tvmedicine.RetrifitService

import com.tvmedicine.API

object Common {
    private const val BASE_URL = "http://www.u1554079.isp.regruhosting.ru"
    val retrofitService: API
        get() = RetrofitClient.getClient(BASE_URL).create(API::class.java)
}