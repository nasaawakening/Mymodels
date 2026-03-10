package com.mymodels.ui.chat

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mymodels.R
import com.mymodels.models.ChatMessage
import com.mymodels.models.ChatSession

class ChatActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var inputMessage: EditText
    private lateinit var sendButton: Button

    private lateinit var adapter: MessageAdapter

    private lateinit var currentSession: ChatSession

    private val sessions = mutableListOf<ChatSession>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        recyclerView = findViewById(R.id.recyclerView)
        inputMessage = findViewById(R.id.inputMessage)
        sendButton = findViewById(R.id.sendButton)

        adapter = MessageAdapter(mutableListOf())

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        createNewSession()

        sendButton.setOnClickListener {
            sendMessage()
        }
    }

    private fun createNewSession() {

        currentSession = ChatSession(
            id = System.currentTimeMillis(),
            title = "New Chat",
            messages = mutableListOf()
        )

        sessions.add(currentSession)
    }

    private fun sendMessage() {

        val text = inputMessage.text.toString()

        if (text.isEmpty()) return

        ChatMessage(
            text = text,
            role = "user"
         )

        currentSession.messages.add(userMessage)

        adapter.addMessage(userMessage)

        inputMessage.text.clear()

        autoScroll()

        simulateAIResponse(text)
    }

    private fun simulateAIResponse(userText: String) {

        val aiMessage = ChatMessage(
            text = "AI Response: $userText",
            isUser = false
        )

        currentSession.messages.add(aiMessage)

        adapter.addMessage(aiMessage)

        autoScroll()
    }

    private fun autoScroll() {
        recyclerView.scrollToPosition(adapter.itemCount - 1)
    }
}