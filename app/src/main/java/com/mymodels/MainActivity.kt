package com.mymodels

import android.content.Intent
import android.os.Bundle
import com.mymodels.utils.NotificationHelper
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import com.mymodels.ui.models.ModelActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       
        NotificationHelper.createChannel(this)

        val modelsButton = findViewById<Button>(R.id.modelsButton)

        modelsButton.setOnClickListener {
            val intent = Intent(this, ModelActivity::class.java)
            startActivity(intent)
        }
    }
}