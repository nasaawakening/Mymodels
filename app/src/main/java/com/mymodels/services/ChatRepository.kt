package com.mymodels.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mymodels.models.ChatMessage

object ChatRepository {

    private val db = FirebaseFirestore.getInstance()

    fun loadMessages(callback: (List<ChatMessage>) -> Unit) {

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        db.collection("users")
            .document(uid)
            .collection("chats")
            .get()
            .addOnSuccessListener {

                val list = it.toObjects(ChatMessage::class.java)

                callback(list)

            }
    }
}