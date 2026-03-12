package com.mymodels.cloud

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mymodels.models.ChatMessage

object ChatCloudService {

    private val db = FirebaseFirestore.getInstance()

    fun saveMessage(message: ChatMessage) {

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        db.collection("users")
            .document(uid)
            .collection("chats")
            .add(message)

    }

}