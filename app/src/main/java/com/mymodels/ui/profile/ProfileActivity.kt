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

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val avatar = findViewById<ImageView>(R.id.avatar)
        val name = findViewById<TextView>(R.id.name)

        val user = FirebaseAuth.getInstance().currentUser

        ProfileLoader.loadAvatar(avatar, user?.photoUrl.toString())

        ProfileService.getProfile {

            val alias = it?.alias

            val displayName = if (alias.isNullOrEmpty())
                user?.displayName
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