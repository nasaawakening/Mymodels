package com.mymodels.ui.models

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mymodels.R
import com.mymodels.utils.ModelManager

class ModelManagerActivity : AppCompatActivity() {

    private lateinit var modelInput: EditText
    private lateinit var downloadButton: Button
    private lateinit var deleteButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_model_manager)

        modelInput = findViewById(R.id.modelInput)
        downloadButton = findViewById(R.id.btnDownload)
        deleteButton = findViewById(R.id.btnDelete)

        downloadButton.setOnClickListener {

            val modelName = modelInput.text.toString().trim()

            if (modelName.isEmpty()) {

                Toast.makeText(this, "Masukkan nama model", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Downloading model...", Toast.LENGTH_SHORT).show()

            Thread {

                try {

                    ModelManager.downloadModel(
                        this,
                        modelName,
                        getModelUrl(modelName)
                    )

                    runOnUiThread {
                        Toast.makeText(
                            this,
                            "Model $modelName berhasil di download",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                } catch (e: Exception) {

                    runOnUiThread {
                        Toast.makeText(
                            this,
                            "Download gagal: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            }.start()
        }

        deleteButton.setOnClickListener {

            val modelName = modelInput.text.toString().trim()

            if (modelName.isEmpty()) {

                Toast.makeText(this, "Masukkan nama model", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            ModelManager.deleteModel(this, modelName)

            Toast.makeText(
                this,
                "Model $modelName berhasil dihapus",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun getModelUrl(name: String): String {

        return when (name.lowercase()) {

            "tinyllama" ->
                "https://huggingface.co/TheBloke/TinyLlama-1.1B-Chat-GGUF/resolve/main/tinyllama-1.1b-chat-v1.0.Q4_K_M.gguf"

            "phi" ->
                "https://huggingface.co/TheBloke/phi-2-GGUF/resolve/main/phi-2.Q4_K_M.gguf"

            "mistral" ->
                "https://huggingface.co/TheBloke/Mistral-7B-Instruct-GGUF/resolve/main/mistral-7b-instruct.Q4_K_M.gguf"

            else ->
                throw Exception("Model tidak dikenal")
        }
    }
}