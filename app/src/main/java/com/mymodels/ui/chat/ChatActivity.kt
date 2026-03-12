package com.mymodels.ui.chat

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mymodels.R
import com.mymodels.models.ChatMessage

class ChatActivity : AppCompatActivity() {

    private lateinit var chatList: RecyclerView
    private lateinit var input: EditText
    private lateinit var sendButton: Button
    private lateinit var adapter: MessageAdapter

    private val messages = mutableListOf<ChatMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatList = findViewById(R.id.chatList)
        input = findViewById(R.id.input)
        sendButton = findViewById(R.id.sendButton)

        adapter = MessageAdapter(messages)

        chatList.layoutManager = LinearLayoutManager(this)
        chatList.adapter = adapter

        sendButton.setOnClickListener {

            val text = input.text.toString()

            if (text.isNotEmpty()) {

                val userMessage = ChatMessage(
                    text = text,
                    role = "user"
                )

                adapter.addMessage(userMessage)

                val aiMessage = ChatMessage(
                    text = "AI: $text",
                    role = "assistant"
                )

                adapter.addMessage(aiMessage)

                input.text.clear()

                chatList.scrollToPosition(messages.size - 1)
            }
             
                ProfileService.getAlias { alias ->

                val googleName =
                com.google.firebase.auth.FirebaseAuth.getInstance()
                .currentUser?.displayName ?: "User"

                val name = alias ?: googleName

                val response =
                AIService.generate(name, prompt)

            }
        }
    }
}