package com.mymodels.ui.chat

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.mymodels.R
import com.mymodels.models.ChatMessage
import com.mymodels.services.AIService
import com.mymodels.services.ProfileService

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

            val prompt = input.text.toString()

            if (prompt.isNotEmpty()) {

                val userMessage = ChatMessage(
                    text = prompt,
                    isUser = true
                )

                adapter.addMessage(userMessage)

                input.text.clear()

                chatList.scrollToPosition(messages.size - 1)

                ProfileService.getAlias { alias ->

                    val googleName =
                        FirebaseAuth.getInstance().currentUser?.displayName ?: "User"

                    val name = alias ?: googleName

                    val aiResponse = AIService.generate(name, prompt)

                    val aiMessage = ChatMessage(
                        text = aiResponse,
                        isUser = false
                    )

                    runOnUiThread {

                        adapter.addMessage(aiMessage)

                        chatList.scrollToPosition(messages.size - 1)

                    }
                }
            }
        }
    }
}