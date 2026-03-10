package com.mymodels.storage

import android.content.Context
import com.google.gson.Gson
import com.mymodels.data.ChatSession
import java.io.File

object ChatStorage {

    private val gson = Gson()

    fun save(context: Context, sessions: List<ChatSession>) {

        val json = gson.toJson(sessions)

        val file = File(context.filesDir, "chats.json")

        file.writeText(json)
    }

    fun load(context: Context): MutableList<ChatSession> {

        val file = File(context.filesDir, "chats.json")

        if (!file.exists()) return mutableListOf()

        val json = file.readText()

        val type = Array<ChatSession>::class.java

        return gson.fromJson(json, type).toMutableList()
   }
    
   fun export(context: Context, session: ChatSession) {

        val json = gson.toJson(session)

        val file = File(
        context.getExternalFilesDir(null),
        "${session.title}.json"
       )

        file.writeText(json)
   }
}