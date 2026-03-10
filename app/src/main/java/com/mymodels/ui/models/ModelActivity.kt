package com.mymodels.ui.models

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mymodels.R

class ModelActivity : AppCompatActivity() {

    private lateinit var modelsList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_models)

        modelsList = findViewById(R.id.modelsList)
        modelsList.layoutManager = LinearLayoutManager(this)
    }
}