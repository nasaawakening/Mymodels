package com.mymodels.utils

import android.content.Context
import java.io.File
import java.net.URL

object ModelManager {

    private fun modelDir(context: Context): File {

        val dir = File(context.filesDir, "models")

        if (!dir.exists()) {
            dir.mkdirs()
        }

        return dir
    }

    // download model dari URL
    fun downloadModel(context: Context, name: String, url: String) {

        val file = File(modelDir(context), "$name.gguf")

        if (file.exists()) return

        val connection = URL(url).openStream()

        file.outputStream().use { output ->
            connection.copyTo(output)
        }
    }

    // hapus model
    fun deleteModel(context: Context, name: String) {

        val file = File(modelDir(context), "$name.gguf")

        if (file.exists()) {
            file.delete()
        }
    }

    // cek apakah ada model
    fun hasModel(context: Context): Boolean {

        val dir = modelDir(context)

        return dir.listFiles()?.isNotEmpty() == true
    }

    // cek model tertentu
    fun hasModel(context: Context, name: String): Boolean {

        val file = File(modelDir(context), "$name.gguf")

        return file.exists()
    }

    // list model
    fun listModels(context: Context): List<String> {

        val files = modelDir(context).listFiles() ?: return emptyList()

        return files.map { it.nameWithoutExtension }
    }
}