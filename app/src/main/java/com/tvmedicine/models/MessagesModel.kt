package com.tvmedicine.models

import java.util.*

data class MessagesModel(
    val messageId: Int,
    val chatId: Int,
    val atUserId: Int,
    val userType: String,
    val text: String,
    val messageDateTime: Date
    )


