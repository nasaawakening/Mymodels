package com.mymodels

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val chatButton = findViewById<Button>(R.id.chatButton)

        chatButton.setOnClickListener {

            startActivity(Intent(this, ChatActivity::class.java))

        }
    }
}
