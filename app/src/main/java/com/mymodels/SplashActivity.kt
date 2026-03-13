package com.mymodels

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.mymodels.ui.auth.LoginActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME = 2000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {

            delay(SPLASH_TIME)

            try {

                val user = FirebaseAuth.getInstance().currentUser

                val next = if (user != null) {
                    MainActivity::class.java
                } else {
                    LoginActivity::class.java
                }

                startActivity(Intent(this@SplashActivity, next))
                finish()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}