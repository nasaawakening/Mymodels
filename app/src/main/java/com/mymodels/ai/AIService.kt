package com.mymodels.ai

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat

class AIService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        startForeground(1, NotificationHelper.runningNotification(this))

        Thread {

            val prompt = intent?.getStringExtra("prompt") ?: ""

            val answer = runModel(prompt)

            NotificationHelper.answerNotification(this, answer)

            stopSelf()

        }.start()

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    fun runModel(prompt: String): String {

        // TODO llama.cpp inference

        Thread.sleep(3000)

        return "AI Response"
    }
}