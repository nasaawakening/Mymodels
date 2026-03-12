package com.mymodels.ui.models

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.mymodels.R
import com.mymodels.utils.ModelManager

class ModelManagerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_model_manager)

        val download = findViewById<Button>(R.id.btnDownload)
        val delete = findViewById<Button>(R.id.btnDelete)

        download.setOnClickListener {

            ModelManager.downloadModel(this, "phi")

        }

        delete.setOnClickListener {

            ModelManager.deleteModel(this, "phi")

        }
    }
}