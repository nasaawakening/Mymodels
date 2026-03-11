package com.mymodels

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mymodels.adapters.ChatAdapter
import com.mymodels.models.ChatMessage
import com.mymodels.services.AIService
import com.mymodels.utils.ModelManager

class MainActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var input: EditText
    private lateinit var send: ImageButton
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var chatLayout: View
    private lateinit var emptyLayout: View

    private val messages = mutableListOf<ChatMessage>()
    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler = findViewById(R.id.chatRecycler)
        input = findViewById(R.id.inputMessage)
        send = findViewById(R.id.btnSend)

        chatLayout = findViewById(R.id.chatLayout)
        emptyLayout = findViewById(R.id.emptyLayout)

        recycler.layoutManager = LinearLayoutManager(this)

        adapter = ChatAdapter(messages)
        recycler.adapter = adapter

        checkModel()

        send.setOnClickListener {

            sendMessage()

        }

        findViewById<Button>(R.id.btnDownloadModel).setOnClickListener {

            startActivity(
                Intent(this, ModelManagerActivity::class.java)
            )

        }

    }

    private fun checkModel() {

        if (ModelManager.hasModel(this)) {

            chatLayout.visibility = View.VISIBLE
            emptyLayout.visibility = View.GONE

        } else {

            chatLayout.visibility = View.GONE
            emptyLayout.visibility = View.VISIBLE

        }

    }

    private fun sendMessage() {

        val text = input.text.toString()

        if (text.isEmpty()) return

        val userMessage = ChatMessage(text, true)

        messages.add(userMessage)
        adapter.notifyItemInserted(messages.size - 1)

        recycler.scrollToPosition(messages.size - 1)

        input.setText("")

        aiReply(text)

    }

    private fun aiReply(prompt: String) {

        val typing = ChatMessage("...", false)

        messages.add(typing)
        adapter.notifyItemInserted(messages.size - 1)

        recycler.scrollToPosition(messages.size - 1)

        recycler.postDelayed({

            messages.remove(typing)

            val reply = AIService.generate(prompt)

            val aiMessage = ChatMessage(reply, false)

            messages.add(aiMessage)

            adapter.notifyDataSetChanged()

            recycler.scrollToPosition(messages.size - 1)

        }, 1000)

    }

}