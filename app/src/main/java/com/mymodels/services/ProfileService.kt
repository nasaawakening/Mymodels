package com.mymodels.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object ProfileService {

    private val db = FirebaseFirestore.getInstance()

    fun getAlias(callback: (String?) -> Unit) {

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {

                callback(it.getString("alias"))
            }
    }

    fun saveAlias(alias: String) {

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        db.collection("users")
            .document(uid)
            .set(mapOf("alias" to alias))
    }

    fun getProfile(callback: (String?) -> Unit) {

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {

                callback(it.getString("alias"))
            }
    }
}