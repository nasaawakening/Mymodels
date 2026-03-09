package com.mymodels.api

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray

class OllamaAPI {

    private val client=OkHttpClient()

    fun listModels():List<String>{

        val request=Request.Builder()
            .url("https://ollama.ai/library")
            .build()

        val response=client.newCall(request).execute()

        val json=JSONArray(response.body!!.string())

        val models= mutableListOf<String>()

        for(i in 0 until json.length()){
            models.add(json.getJSONObject(i).getString("name"))
        }

        return models
    }
}
