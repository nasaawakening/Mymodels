package com.mymodels.ui.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import android.widget.EditText
import com.mymodels.R

class ChatActivity : AppCompatActivity() {

    private lateinit var chatList: RecyclerView
    private lateinit var input: EditText
    private lateinit var sendButton: Button
    private lateinit var newChatButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        newChatButton = findViewById(R.id.newChatButton)

       newChatButton.setOnClickListener {

        messages.clear()
        chatList.adapter?.notifyDataSetChanged()

      }

        chatList = findViewById(R.id.chatList)
        input = findViewById(R.id.input)
        sendButton = findViewById(R.id.sendButton)

        chatList.layoutManager = LinearLayoutManager(this)

        sendButton.setOnClickListener {
            val message = input.text.toString()
            input.setText("")
        }

        NotificationHelper.show(
        this,
        "AI selesai menjawab",
        "Balasan model sudah siap")

    }
}