package com.mymodels.data

data class ChatSession(
    val id: Long,
    val messages: MutableList<String>
)