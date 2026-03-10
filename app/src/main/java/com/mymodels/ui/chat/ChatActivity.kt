package com.mymodels.ui.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import android.widget.EditText
import android.widget.SearchView
import com.mymodels.R
import com.mymodels.data.ChatSession
import com.mymodels.ui.history.HistoryAdapter
import com.mymodels.utils.NotificationHelper

class ChatActivity : AppCompatActivity() {

    private lateinit var chatList: RecyclerView
    private lateinit var historyList: RecyclerView

    private lateinit var input: EditText
    private lateinit var sendButton: Button
    private lateinit var newChatButton: Button
    private lateinit var searchChat: SearchView

    private val sessions = mutableListOf<ChatSession>()
    private var currentSession: ChatSession? = null

    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_chat)
     
        //=====================
        // SEND CHAT
        //=====================

          val intent = Intent(this, AIService::class.java)
          intent.putExtra("prompt", userMessage)

          startForegroundService(intent)

        // =====================
        // FIND VIEW
        // =====================

        chatList = findViewById(R.id.chatList)
        historyList = findViewById(R.id.historyList)

        input = findViewById(R.id.input)
        sendButton = findViewById(R.id.sendButton)
        newChatButton = findViewById(R.id.newChatButton)

        searchChat = findViewById(R.id.searchChat)

        // =====================
        // CHAT LIST
        // =====================

        chatList.layoutManager = LinearLayoutManager(this)

        // =====================
        // HISTORY SIDEBAR
        // =====================

        historyAdapter = HistoryAdapter(sessions) { session ->

        currentSession = session

        chatList.adapter?.notifyDataSetChanged()

       }

        historyList.adapter = historyAdapter

        historyList.layoutManager = LinearLayoutManager(this)
        historyList.adapter = historyAdapter

        // =====================
        // FIRST CHAT SESSION
        // =====================

        val firstSession = ChatSession(
            title = "New Chat",
            id = System.currentTimeMillis(),
            messages = mutableListOf()
        )

        sessions.add(firstSession)

        currentSession = firstSession

        // =====================
        // NEW CHAT BUTTON
        // =====================

        newChatButton.setOnClickListener {

            val newSession = ChatSession(
                title = "New Chat",
                id = System.currentTimeMillis(),
                messages = mutableListOf()
            )

            sessions.add(newSession)

            currentSession = newSession

            historyAdapter.notifyDataSetChanged()

        }

        // =====================
        // SEND MESSAGE
        // =====================

        sendButton.setOnClickListener {

            val message = input.text.toString()

            if (message.isEmpty()) return@setOnClickListener

            input.setText("")

            currentSession?.messages?.add(message)

            // Auto title chat dari pesan pertama
            if (currentSession?.messages?.size == 1) {

                currentSession?.title = message.take(30)

                historyAdapter.notifyDataSetChanged()

            }

            chatList.adapter?.notifyDataSetChanged()

            // Simulasi AI selesai menjawab
            NotificationHelper.show(
                this,
                "AI selesai menjawab",
                "Balasan model sudah siap"
            )
        }

        // =====================
        // SEARCH CHAT HISTORY
        // =====================

        searchChat.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                val filtered = sessions.filter {

                    it.title.contains(newText ?: "", true)

                }

                historyAdapter.updateList(filtered)

                return true
            }

        })

    }
}