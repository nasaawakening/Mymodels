package com.mymodels

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mymodels.adapters.HistoryAdapter
import com.mymodels.models.ChatMessage
import com.mymodels.services.AIService

class MainActivity : AppCompatActivity() {

    private lateinit var chatRecycler: RecyclerView
    private lateinit var emptyView: LinearLayout
    private lateinit var inputMessage: EditText
    private lateinit var btnSend: ImageButton
    private lateinit var adapter: HistoryAdapter

    private val messages = mutableListOf<ChatMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        chatRecycler = findViewById(R.id.chatRecycler)
        emptyView = findViewById(R.id.emptyView)
        inputMessage = findViewById(R.id.inputMessage)
        btnSend = findViewById(R.id.btnSend)

        adapter = HistoryAdapter(messages)

        chatRecycler.layoutManager = LinearLayoutManager(this)
        chatRecycler.adapter = adapter

        checkModelStatus()

        btnSend.setOnClickListener {

            val text = inputMessage.text.toString()

            if (text.isNotEmpty()) {

                val userMessage = ChatMessage(text, true)
                messages.add(userMessage)
                adapter.notifyItemInserted(messages.size - 1)

                inputMessage.text.clear()

                val aiReply = AIService.generateReply(text)

                val aiMessage = ChatMessage(aiReply, false)
                messages.add(aiMessage)
                adapter.notifyItemInserted(messages.size - 1)

                chatRecycler.scrollToPosition(messages.size - 1)
            }

        }

        findViewById<View>(R.id.btnSelectModel).setOnClickListener {
            startActivity(Intent(this, ModelActivity::class.java))
        }
    }

    private fun checkModelStatus() {

        val modelLoaded = false

        if (modelLoaded) {

            emptyView.visibility = View.GONE
            chatRecycler.visibility = View.VISIBLE

        } else {

            emptyView.visibility = View.VISIBLE
            chatRecycler.visibility = View.GONE

        }

    }
}