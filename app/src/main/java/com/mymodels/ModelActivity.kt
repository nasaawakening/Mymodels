package com.mymodels.utils

import android.content.Context
import com.mymodels.models.AIModel
import java.io.File

object ModelManager {

    var activeModel: String? = null

    fun hasModel(context: Context): Boolean {

        val dir = File(context.filesDir, "models")

        return dir.exists() && dir.listFiles()?.isNotEmpty() == true

    }

    fun getModels(context: Context): List<AIModel> {

        val dir = File(context.filesDir, "models")

        if (!dir.exists()) dir.mkdirs()

        val models = mutableListOf<AIModel>()

        dir.listFiles()?.forEach {

            models.add(
                AIModel(it.name, it.absolutePath)
            )

        }

        return models

    }

    fun setActiveModel(name: String) {

        activeModel = name

    }

}