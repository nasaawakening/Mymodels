package com.mymodels.api

import okhttp3.OkHttpClient
import okhttp3.Request

class OllamaAPI(
    private val baseUrl: String,
    private val token: String?
) {

    private val client = OkHttpClient()

    fun listModels(): String {

        val url = "$baseUrl/api/tags"

        val requestBuilder = Request.Builder()
            .url(url)

        if (token != null) {

            requestBuilder.addHeader(
                "Authorization",
                "Bearer $token"
            )

        }

        val request = requestBuilder.build()

        val response = client.newCall(request).execute()

        return response.body!!.string()
    }

}
