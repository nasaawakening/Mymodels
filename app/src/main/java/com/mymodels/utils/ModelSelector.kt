package com.mymodels.utils

import android.content.Context

object ModelSelector {

    private const val PREF = "mymodels"
    private const val KEY_MODEL = "active_model"

    fun setModel(context: Context, model: String) {

        val prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)

        prefs.edit().putString(KEY_MODEL, model).apply()
    }

    fun getModel(context: Context): String {

        val prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)

        return prefs.getString(KEY_MODEL, "phi") ?: "phi"
    }

}