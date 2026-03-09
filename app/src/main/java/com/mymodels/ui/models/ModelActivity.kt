package com.mymodels.ui.models

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mymodels.R
import com.mymodels.ui.chat.ChatActivity
import kotlinx.android.synthetic.main.activity_models.*

class ModelsActivity : AppCompatActivity() {

    private lateinit var adapter: ModelAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_models)

        adapter = ModelAdapter { model ->

            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("model", model)

            startActivity(intent)
        }

        modelsList.layoutManager = LinearLayoutManager(this)
        modelsList.adapter = adapter

        loadModels()
    }

    private fun loadModels() {

        val models = listOf(
            "llama-3-gguf",
            "mistral-gguf",
            "phi-2-gguf"
        )

        adapter.submit(models)
    }
}
