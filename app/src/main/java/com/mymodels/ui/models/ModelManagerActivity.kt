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

            val modelName = modelInput.text.toString()

            if (modelName.isEmpty()) {

                Toast.makeText(this, "Masukkan nama model", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            ModelManager.downloadModel(this, modelName)

            Toast.makeText(this, "Model $modelName berhasil di download", Toast.LENGTH_SHORT).show()
        }

        deleteButton.setOnClickListener {

            val modelName = modelInput.text.toString()

            if (modelName.isEmpty()) {

                Toast.makeText(this, "Masukkan nama model", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            ModelManager.deleteModel(this, modelName)

            Toast.makeText(this, "Model $modelName berhasil dihapus", Toast.LENGTH_SHORT).show()
        }
    }
}