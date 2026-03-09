package com.mymodels.ui.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mymodels.R
import com.mymodels.ai.engine.GGUFRunner
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {

    private val adapter = MessageAdapter()
    private val runner = GGUFRunner()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatList.layoutManager = LinearLayoutManager(this)
        chatList.adapter = adapter

        val model = intent.getStringExtra("model") ?: ""

        sendButton.setOnClickListener {

            val prompt = input.text.toString()

            if(prompt.isEmpty()) return@setOnClickListener

            adapter.add(Message(prompt,true))

            input.setText("")

            Thread {

                val response = runner.prompt(prompt)

                runOnUiThread {
                    adapter.add(Message(response,false))
                    chatList.scrollToPosition(adapter.itemCount-1)
                }

            }.start()
        }
    }
}
