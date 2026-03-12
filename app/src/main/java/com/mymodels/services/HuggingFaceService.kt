package com.mymodels.services

import okhttp3.*

object HuggingFaceService {

    private val client = OkHttpClient()

    fun uploadModel(token: String, repo: String, fileContent: String) {

        val body = RequestBody.create(
            "application/json".toMediaTypeOrNull(),
            fileContent
        )

        val request = Request.Builder()
            .url("https://huggingface.co/api/repos/create")
            .addHeader("Authorization", "Bearer $token")
            .post(body)
            .build()

        client.newCall(request).execute()

    }

}