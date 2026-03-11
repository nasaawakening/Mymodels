package com.mymodels.utils

import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

object ProfileLoader {

    fun load(avatar: ImageView, name: TextView, email: TextView) {

        val user = FirebaseAuth.getInstance().currentUser ?: return

        name.text = user.displayName
        email.text = user.email

        Glide.with(avatar.context)
            .load(user.photoUrl)
            .into(avatar)

    }

}