package com.mymodels.ui.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
    private lateinit var btnEmailLogin: Button
    private lateinit var btnGithubLogin: LinearLayout
    private lateinit var btnGuest: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)

        btnEmailLogin = findViewById(R.id.btnLoginEmail)
        btnGithubLogin = findViewById(R.id.btnLoginGithub)
        btnGuest = findViewById(R.id.btnGuest)

        val subtitle = findViewById<TextView>(R.id.subtitle)

        typeText(subtitle, "Running AI models locally...")

        checkLoginSession()

        btnEmailLogin.setOnClickListener {
            loginEmail()
        }

        btnGithubLogin.setOnClickListener {
            loginGithub()
        }

        btnGuest.setOnClickListener {
            loginGuest()
        }
    }

    private fun typeText(textView: TextView, text: String, delay: Long = 40) {

        var index = 0
        textView.text = ""

        val handler = Handler(Looper.getMainLooper())

        val runnable = object : Runnable {

            override fun run() {

                if (index < text.length) {

                    textView.append(text[index].toString())
                    index++

                    handler.postDelayed(this, delay)
                }
            }
        }

        handler.post(runnable)
    }

    private fun checkLoginSession() {

        val user = auth.currentUser

        if (user != null) {
            openMain()
        }
    }

    private fun loginEmail() {

        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {

            Toast.makeText(this, "Email dan password wajib diisi", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {

                if (it.isSuccessful) {

                    Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()
                    openMain()

                } else {

                    Toast.makeText(this, "Login gagal", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun loginGithub() {

        val provider = OAuthProvider.newBuilder("github.com")

        val pendingResult = auth.pendingAuthResult

        if (pendingResult != null) {

            pendingResult
                .addOnSuccessListener {
                    openMain()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "GitHub login gagal", Toast.LENGTH_SHORT).show()
                }

        } else {

            auth.startActivityForSignInWithProvider(this, provider.build())
                .addOnSuccessListener {
                    openMain()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "GitHub login gagal", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun loginGuest() {

        auth.signInAnonymously()
            .addOnCompleteListener {

                if (it.isSuccessful) {

                    Toast.makeText(this, "Guest mode aktif", Toast.LENGTH_SHORT).show()
                    openMain()

                } else {

                    Toast.makeText(this, "Guest login gagal", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun openMain() {

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}