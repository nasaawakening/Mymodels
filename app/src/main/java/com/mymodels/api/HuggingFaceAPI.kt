package com.mymodels.api

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray

class HuggingFaceAPI(private val token:String?) {

    private val client = OkHttpClient()

    fun searchModels(query:String):List<String>{

        val url="https://huggingface.co/api/models?search=$query"

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization","Bearer $token")
            .build()

        val response=client.newCall(request).execute()
        val json=JSONArray(response.body!!.string())

        val list= mutableListOf<String>()

        for(i in 0 until json.length()){
            list.add(json.getJSONObject(i).getString("id"))
        }

        return list
    }
}
