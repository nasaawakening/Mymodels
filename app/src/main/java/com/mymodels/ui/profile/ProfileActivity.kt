package com.mymodels.ui.profile

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.mymodels.R
import com.mymodels.services.ProfileService
import com.mymodels.utils.ProfileLoader

class ProfileActivity : AppCompatActivity() {

    private lateinit var avatar: ImageView
    private lateinit var name: TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        avatar = findViewById(R.id.avatar)
        name = findViewById(R.id.name)

        val user = FirebaseAuth.getInstance().currentUser

        val photoUrl = user?.photoUrl?.toString()

        ProfileLoader.loadAvatar(avatar, photoUrl)

        ProfileService.getProfile { alias ->

            val googleName = user?.displayName ?: "User"

            val displayName =
                if (alias.isNullOrEmpty())
                    googleName
                else
                    alias

            name.text = displayName
        }

        avatar.setOnClickListener {

            startActivity(
                Intent(this, EditProfileActivity::class.java)
            )
        }
    }
}