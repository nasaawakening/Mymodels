package com.mymodels.ui.models

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.mymodels.R
import com.mymodels.utils.ModelManager

class ModelManagerActivity : AppCompatActivity() {

    private lateinit var statusText: TextView
    private lateinit var downloadBtn: Button
    private lateinit var deleteBtn: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_model_manager)

        statusText = findViewById(R.id.modelStatus)
        downloadBtn = findViewById(R.id.btnDownloadModel)
        deleteBtn = findViewById(R.id.btnDeleteModel)
        progressBar = findViewById(R.id.modelProgress)

        updateStatus()

        downloadBtn.setOnClickListener {

            downloadModel()

        }

        deleteBtn.setOnClickListener {

            deleteModel()

        }
    }

    private fun updateStatus() {

        if (ModelManager.hasModel(this)) {

            statusText.text = "Model installed"
            deleteBtn.isEnabled = true
            downloadBtn.isEnabled = false

        } else {

            statusText.text = "No model installed"
            deleteBtn.isEnabled = false
            downloadBtn.isEnabled = true

        }
    }

    private fun downloadModel() {

        progressBar.visibility = ProgressBar.VISIBLE
        statusText.text = "Downloading model..."

        Thread {

            ModelManager.downloadModel(this)

            runOnUiThread {

                progressBar.visibility = ProgressBar.GONE

                Toast.makeText(
                    this,
                    "Model downloaded successfully",
                    Toast.LENGTH_LONG
                ).show()

                updateStatus()

            }

        }.start()
    }

    private fun deleteModel() {

        ModelManager.deleteModel(this)

        Toast.makeText(
            this,
            "Model deleted",
            Toast.LENGTH_SHORT
        ).show()

        updateStatus()
    }
}