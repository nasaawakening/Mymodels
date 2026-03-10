package com.mymodels.models

data class ChatSession(
    val id: Long,
    val title: String,
    val messages: MutableList<ChatMessage>
)