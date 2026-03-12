package com.mymodels.services

import android.content.Context
import java.io.File

object OllamaExporter {

    fun export(context: Context, modelName: String, base: String, prompt: String) {

        val modelfile = """
FROM $base

SYSTEM "$prompt"
""".trimIndent()

        val file = File(context.filesDir, "Modelfile_$modelName")

        file.writeText(modelfile)

    }

}