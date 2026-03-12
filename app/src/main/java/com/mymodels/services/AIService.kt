package com.mymodels.services

object AIService {

    fun generate(name: String, prompt: String, model: String): String {

        return when(model) {

            "phi" -> phiModel(name, prompt)

            "mistral" -> mistralModel(name, prompt)

            "llama" -> llamaModel(name, prompt)

            else -> "Model not found"

        }

    }

    private fun phiModel(name: String, prompt: String): String {

        return """
Hello $name 👋

You asked:

$prompt

This response is from **Phi Model**
"""
    }

    private fun mistralModel(name: String, prompt: String): String {

        return """
Hello $name 👋

You asked:

$prompt

This response is from **Mistral Model**
"""
    }

    private fun llamaModel(name: String, prompt: String): String {

        return """
Hello $name 👋

You asked:

$prompt

This response is from **Llama Model**
"""
    }

}