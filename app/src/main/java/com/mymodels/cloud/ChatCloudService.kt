package com.mymodels.cloud

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.mymodels.models.ChatMessage

object ChatCloudService {

    private val db = FirebaseFirestore.getInstance()

    fun saveMessage(message: ChatMessage) {

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val data = hashMapOf(
            "text" to message.text,
            "isUser" to message.isUser,
            "time" to System.currentTimeMillis()
        )

        db.collection("users")
            .document(uid)
            .collection("chats")
            .add(data)

    }

    fun loadMessages(callback: (List<ChatMessage>) -> Unit) {

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        db.collection("users")
            .document(uid)
            .collection("chats")
            .orderBy("time")
            .get()
            .addOnSuccessListener {

                val list = it.toObjects(ChatMessage::class.java)

                callback(list)

            }

    }

}