package com.mymodels.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import com.mymodels.MainActivity
import com.mymodels.R

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText

    private lateinit var loginEmailButton: Button
    private lateinit var loginGithubButton: Button
    private lateinit var guestButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)

        loginEmailButton = findViewById(R.id.btnLoginEmail)
        loginGithubButton = findViewById(R.id.btnLoginGithub)
        guestButton = findViewById(R.id.btnGuest)

        loginEmailButton.setOnClickListener {
            loginWithEmail()
        }

        loginGithubButton.setOnClickListener {
            loginWithGithub()
        }

        guestButton.setOnClickListener {
            loginAsGuest()
        }
    }

    private fun loginWithEmail() {

        val email = emailInput.text.toString()
        val password = passwordInput.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email atau password kosong", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {

                if (it.isSuccessful) {
                    openMain()
                } else {
                    Toast.makeText(this, "Login gagal", Toast.LENGTH_SHORT).show()
                }

            }
    }

    private fun loginWithGithub() {

        val provider = OAuthProvider.newBuilder("github.com")

        auth.startActivityForSignInWithProvider(this, provider.build())
            .addOnSuccessListener {

                Toast.makeText(this, "Login GitHub berhasil", Toast.LENGTH_SHORT).show()
                openMain()

            }
            .addOnFailureListener {

                Toast.makeText(this, "Login GitHub gagal", Toast.LENGTH_SHORT).show()

            }
    }

    private fun loginAsGuest() {

        Toast.makeText(this, "Masuk sebagai Guest", Toast.LENGTH_SHORT).show()
        openMain()

    }

    private fun openMain() {

        startActivity(Intent(this, MainActivity::class.java))
        finish()

    }
}