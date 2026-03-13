package com.mymodels

import android.app.Application

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val prefs = getSharedPreferences("app", MODE_PRIVATE)

        prefs.edit().putBoolean("running", true).apply()

        Thread.setDefaultUncaughtExceptionHandler { _, _ ->
            prefs.edit()
                .putBoolean("crashed", true)
                .putBoolean("running", false)
                .apply()

            android.os.Process.killProcess(android.os.Process.myPid())
        }
    }
}