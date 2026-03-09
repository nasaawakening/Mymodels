package com.mymodels.utils

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("mymodels_tokens", Context.MODE_PRIVATE)

    companion object {
        private const val HF_TOKEN = "huggingface_token"
        private const val OLLAMA_TOKEN = "ollama_token"
    }

    // =========================
    // HUGGINGFACE TOKEN
    // =========================

    fun saveHuggingFaceToken(token: String) {
        prefs.edit().putString(HF_TOKEN, token).apply()
    }

    fun getHuggingFaceToken(): String? {
        return prefs.getString(HF_TOKEN, null)
    }

    fun clearHuggingFaceToken() {
        prefs.edit().remove(HF_TOKEN).apply()
    }

    // =========================
    // OLLAMA TOKEN
    // =========================

    fun saveOllamaToken(token: String) {
        prefs.edit().putString(OLLAMA_TOKEN, token).apply()
    }

    fun getOllamaToken(): String? {
        return prefs.getString(OLLAMA_TOKEN, null)
    }

    fun clearOllamaToken() {
        prefs.edit().remove(OLLAMA_TOKEN).apply()
    }

    // =========================
    // CLEAR ALL
    // =========================

    fun clearAllTokens() {
        prefs.edit().clear().apply()
    }
}
