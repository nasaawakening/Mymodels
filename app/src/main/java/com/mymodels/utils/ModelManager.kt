package com.mymodels.utils

import android.content.Context
import java.io.File

object ModelManager {

    private const val MODEL_FILE = "model.bin"

    fun hasModel(context: Context): Boolean {

        val file = File(context.filesDir, MODEL_FILE)

        return file.exists()
    }

    fun downloadModel(context: Context) {

        val file = File(context.filesDir, MODEL_FILE)

        file.writeText("dummy model")

    }

    fun deleteModel(context: Context) {

        val file = File(context.filesDir, MODEL_FILE)

        if (file.exists()) file.delete()

    }

}