package com.mymodels.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

object FirebaseAuthManager {

    private val auth = FirebaseAuth.getInstance()

    fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        auth.signInWithCredential(credential)

    }

    fun currentUser() = auth.currentUser

    fun logout() {

        auth.signOut()

    }

}