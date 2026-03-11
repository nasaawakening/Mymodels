package com.mymodels.services

object AIService {

    fun generate(prompt: String): String {

        return """
Hello Nasa 👋

You said:

**$prompt**

Here is an example response with Markdown support.

### Example List
- Item 1
- Item 2
- Item 3

### Example Code

```kotlin
fun greet(){
    println("Hello Nasa")
}