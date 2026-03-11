package com.mymodels.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.mymodels.R
import com.mymodels.utils.NotificationHelper

class AIService : Service() {

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {

        val prompt = intent?.getStringExtra("prompt") ?: ""

        // jalankan AI di thread terpisah
        Thread {

            val response = runModel(prompt)

            // kirim notifikasi jika AI selesai
            NotificationHelper.show(
                this,
                response
            )

            stopSelf()

        }.start()

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    // =====================
    // AI MODEL INFERENCE
    // =====================

    private fun runModel(prompt: String): String {

        // simulasi proses AI
        Thread.sleep(3000)

        // nanti di sini bisa pakai
        // llama.cpp
        // Ollama API
        // HuggingFace API

        return "Jawaban AI untuk: $prompt"
    }
}