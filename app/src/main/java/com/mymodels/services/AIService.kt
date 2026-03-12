package com.mymodels.services

object AIService {

    fun sendMessage(prompt: String, callback: (String) -> Unit) {

        val response = """
Hello 👋

You said:
$prompt
"""

        callback(response)
    }

}