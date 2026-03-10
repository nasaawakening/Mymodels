package com.mymodels.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mymodels.app.adapters.HistoryAdapter
import com.mymodels.app.models.ChatMessage
import com.mymodels.app.services.AIService
import com.mymodels.app.utils.NotificationHelper

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HistoryAdapter
    private lateinit var inputMessage: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var emptyView: View
    private lateinit var selectModelButton: Button

    private val messages = mutableListOf<ChatMessage>()

    private fun checkModelStatus() {

     val modelFolder = File(filesDir, "models")

     val modelLoaded =
         modelFolder.exists() &&
         modelFolder.listFiles()?.isNotEmpty() == true

     if (modelLoaded) {

         emptyView.visibility = View.GONE
         chatRecycler.visibility = View.VISIBLE

     } else {

         emptyView.visibility = View.VISIBLE
         chatRecycler.visibility = View.GONE

     }
 }

    private lateinit var aiService: AIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.chatRecycler)
        inputMessage = findViewById(R.id.inputMessage)
        sendButton = findViewById(R.id.btnSend)

        emptyView = findViewById(R.id.emptyView)
        selectModelButton = findViewById(R.id.btnSelectModel)

        aiService = AIService(this)

        setupRecycler()
        loadHistory()
        checkModelStatus()

        sendButton.setOnClickListener {
            sendMessage()
        }

        selectModelButton.setOnClickListener {

            startActivity(
                Intent(this, ModelActivity::class.java)
            )

        }
    }

    private fun setupRecycler() {

        adapter = HistoryAdapter(messages)

        recyclerView.layoutManager =
            LinearLayoutManager(this)

        recyclerView.adapter = adapter

    }

    private fun sendMessage() {

        val text = inputMessage.text.toString().trim()

        if (text.isEmpty()) return

        val userMessage = ChatMessage(
            text = text,
            isUser = true
        )

        addMessage(userMessage)

        inputMessage.setText("")

        NotificationHelper.showNotification(
            this,
            "You",
            text
        )

        aiService.sendMessage(text) { response ->

            runOnUiThread {

                val aiMessage = ChatMessage(
                    text = response,
                    isUser = false
                )

                addMessage(aiMessage)

                saveHistory()

                NotificationHelper.showNotification(
                    this,
                    "MyModels AI",
                    response
                )

            }

        }

    }

    private fun addMessage(message: ChatMessage) {

        messages.add(message)

        adapter.notifyItemInserted(messages.size - 1)

        recyclerView.scrollToPosition(messages.size - 1)

    }

    private fun checkModelStatus() {

        val prefs =
            getSharedPreferences("ai_settings", MODE_PRIVATE)

        val modelLoaded =
            prefs.getBoolean("model_loaded", false)

        if (modelLoaded) {

            emptyView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE

        } else {

            emptyView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE

        }

    }

    private fun loadHistory() {

        val prefs =
            getSharedPreferences("chat_history", MODE_PRIVATE)

        val history =
            prefs.getString("messages", null)

        if (history != null) {

            val items = history.split("|||")

            for (item in items) {

                val parts = item.split("::")

                if (parts.size == 2) {

                    val isUser = parts[0] == "user"
                    val text = parts[1]

                    messages.add(
                        ChatMessage(
                            text,
                            isUser
                        )
                    )

                }

            }

            adapter.notifyDataSetChanged()

            recyclerView.scrollToPosition(messages.size - 1)

        }

    }

    private fun saveHistory() {

        val prefs =
            getSharedPreferences("chat_history", MODE_PRIVATE)

        val editor = prefs.edit()

        val builder = StringBuilder()

        for (msg in messages) {

            val role =
                if (msg.isUser) "user" else "ai"

            builder.append(role)
            builder.append("::")
            builder.append(msg.text)
            builder.append("|||")

        }

        editor.putString("messages", builder.toString())

        editor.apply()

    }

}