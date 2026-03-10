package com.mymodels.data

data class ChatSession(

    var title: String,
    val id: Long,
    val messages: MutableList<ChatMessage>

)