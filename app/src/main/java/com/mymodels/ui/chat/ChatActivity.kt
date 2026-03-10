package com.mymodels.ui.chat

import android.os.Bundle
import android.widget.Button
import com.mymodels.models.ChatMessage
import com.mymodels.models.ChatSession
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mymodels.R
import com.mymodels.utils.NotificationHelper

class ChatActivity : AppCompatActivity() {

    private lateinit var chatList: RecyclerView
    private lateinit var input: EditText
    private lateinit var sendButton: Button
    private lateinit var newChatButton: Button

    private val sessions = mutableListOf<ChatSession>()
    private var currentSession: ChatSession? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_chat)

        chatList = findViewById(R.id.chatList)
        input = findViewById(R.id.input)
        sendButton = findViewById(R.id.sendButton)
        newChatButton = findViewById(R.id.newChatButton)

        chatList.layoutManager = LinearLayoutManager(this)

        val firstSession = ChatSession(
            System.currentTimeMillis(),
            mutableListOf()
        )

        sessions.add(firstSession)
        currentSession = firstSession

        sendButton.setOnClickListener {

            val userMessage = input.text.toString()

            if (userMessage.isEmpty()) return@setOnClickListener

            currentSession?.messages?.add(
                ChatMessage("user", userMessage)
            )

            input.setText("")

            chatList.adapter?.notifyDataSetChanged()

            fakeAI()
        }

        newChatButton.setOnClickListener {

            val newSession = ChatSession(
            id = System.currentTimeMillis(),
            title = "New Chat",
            messages = mutableListOf()
           )

            sessions.add(newSession)
            currentSession = newSession

            chatList.adapter?.notifyDataSetChanged()
        }
    }

    private fun fakeAI() {

        Thread {

            Thread.sleep(2000)

            runOnUiThread {

                currentSession?.messages?.add(
                    ChatMessage("ai", "Ini adalah balasan AI")
                )

                chatList.adapter?.notifyDataSetChanged()

                NotificationHelper.show(
                    this,
                    "AI selesai menjawab"
                )
            }

        }.start()
    }

    fun searchChat(query: String) {

        // nanti untuk fitur search history
    }
}