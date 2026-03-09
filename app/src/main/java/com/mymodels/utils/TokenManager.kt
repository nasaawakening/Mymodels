package com.mymodels.utils

import android.content.Context

class TokenManager(context: Context) {

    private val prefs =
        context.getSharedPreferences("mymodels", Context.MODE_PRIVATE)

    fun saveHFToken(token: String) {

        prefs.edit().putString("hf_token", token).apply()

    }

    fun getHFToken(): String? {

        return prefs.getString("hf_token", null)

    }

    fun saveOllamaToken(token: String) {

        prefs.edit().putString("ollama_token", token).apply()

    }

    fun getOllamaToken(): String? {

        return prefs.getString("ollama_token", null)

    }

}
