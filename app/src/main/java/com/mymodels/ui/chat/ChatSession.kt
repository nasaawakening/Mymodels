package com.mymodels.ui.chat

data class ChatSession(
    val id: Long,
    val messages: MutableList<ChatMessage>
)