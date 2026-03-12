package com.mymodels.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object ProfileService {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun saveAlias(alias: String) {

        val uid = auth.currentUser?.uid ?: return

        val data = mapOf(
            "alias" to alias
        )

        db.collection("users")
            .document(uid)
            .set(data, com.google.firebase.firestore.SetOptions.merge())
    }

    fun getAlias(callback: (String?) -> Unit) {

        val uid = auth.currentUser?.uid ?: return

        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {

                val alias = it.getString("alias")

                callback(alias)

            }
    }
}