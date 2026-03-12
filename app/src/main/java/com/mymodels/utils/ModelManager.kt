package com.mymodels.utils

import android.content.Context
import java.io.File

object ModelManager {

    private fun modelDir(context: Context): File {
        val dir = File(context.filesDir, "models")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    fun hasModel(context: Context): Boolean {
        return modelDir(context).listFiles()?.isNotEmpty() == true
    }

    fun downloadModel(context: Context, name: String) {
        val file = File(modelDir(context), "$name.model")
        file.writeText("dummy ai model")
    }

    fun deleteModel(context: Context, name: String) {
        val file = File(modelDir(context), "$name.model")
        if (file.exists()) file.delete()
    }
}