package com.tvmedicine.models

import java.util.*

data class MessagesModel(
    val message_id: Int,
    val chat_id: Int,
    val at_User_id: Int,
    val user_type: String,
    val text: String,
    val message_date_time: Date
    )


