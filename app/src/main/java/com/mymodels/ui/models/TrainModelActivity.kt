package com.mymodels.ui.models

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.mymodels.R
import com.mymodels.services.ModelCreator

class TrainModelActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_train_model)

        val name = findViewById<EditText>(R.id.modelName)
        val base = findViewById<EditText>(R.id.baseModel)
        val prompt = findViewById<EditText>(R.id.systemPrompt)

        val train = findViewById<Button>(R.id.trainButton)

        train.setOnClickListener {

            ModelCreator.createModel(
                this,
                name.text.toString(),
                base.text.toString(),
                prompt.text.toString()
            )

        }

    }

}