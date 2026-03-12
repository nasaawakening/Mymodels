package com.mymodels.services

import com.google.firebase.auth.FirebaseAuth

object AIService {

    fun generate(userName: String, prompt: String): String {

        return """
Hello $userName 👋

You said:

$prompt

How can I help you today?
""".trimIndent()

    }

    fun sendMessage(prompt: String, callback: (String) -> Unit) {

        val googleName =
            FirebaseAuth.getInstance()
                .currentUser?.displayName ?: "User"

        val response = generate(googleName, prompt)

        callback(response)

    }

}