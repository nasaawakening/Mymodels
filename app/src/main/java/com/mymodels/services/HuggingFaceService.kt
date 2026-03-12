package com.mymodels.services

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

object HuggingFaceService {

    private val client = OkHttpClient()

    fun query(prompt: String): String {

        val body = RequestBody.create(
            "application/json".toMediaTypeOrNull(),
            """{"inputs":"$prompt"}"""
        )

        val request = Request.Builder()
            .url("https://api-inference.huggingface.co/models/gpt2")
            .post(body)
            .build()

        val response = client.newCall(request).execute()

        return response.body?.string() ?: ""
    }
}