package com.mymodels.services

import android.content.Context
import java.io.File
import org.json.JSONObject

object ModelCreator {

    fun createModel(
        context: Context,
        modelName: String,
        baseModel: String,
        systemPrompt: String
    ) {

        val model = JSONObject()

        model.put("name", modelName)
        model.put("base", baseModel)
        model.put("system_prompt", systemPrompt)

        val file = File(context.filesDir, "$modelName.json")

        file.writeText(model.toString())

    }

}