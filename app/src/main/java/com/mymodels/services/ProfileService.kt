package com.mymodels.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object ProfileService {

    fun getAlias(callback: (String?) -> Unit) {

        val uid = FirebaseAuth.getInstance().currentUser?.uid
            ?: return callback(null)

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {

                val alias = it.getString("alias")

                callback(alias)

            }
            .addOnFailureListener {

                callback(null)

            }

    }

}