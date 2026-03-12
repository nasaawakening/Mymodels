package com.mymodels

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.mymodels.ui.auth.LoginActivity

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME: Long = 2000 // 2 detik

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {

            // jika ada layout splash
            setContentView(R.layout.activity_splash)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        Handler(Looper.getMainLooper()).postDelayed({

            try {

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }, SPLASH_TIME)
    }
}