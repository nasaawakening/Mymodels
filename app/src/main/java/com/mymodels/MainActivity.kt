package com.mymodels

import android.app.DownloadManager
import android.content.*
import android.net.Uri
import android.os.*
import android.view.View
import android.widget.*

import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var input: EditText
    private lateinit var send: ImageButton

    private lateinit var loginLayout: View
    private lateinit var chatLayout: View
    private lateinit var emptyLayout: View

    private lateinit var googleSignInClient: GoogleSignInClient

    // =========================================================
    // 🚀 ON CREATE (SAFE STARTUP)
    // =========================================================

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {

            setContentView(R.layout.activity_main)

            initViews()
            initGoogleLogin()
            checkLogin()

        } catch (e: Exception) {
            e.printStackTrace()
            toast("Startup error")
        }
    }

    // =========================================================
    // 🧠 INIT UI
    // =========================================================

    private fun initViews() {

        loginLayout = findViewById(R.id.loginLayout)
        chatLayout = findViewById(R.id.chatLayout)
        emptyLayout = findViewById(R.id.emptyLayout)

        recycler = findViewById(R.id.chatRecycler)
        input = findViewById(R.id.inputMessage)
        send = findViewById(R.id.btnSend)

        recycler.layoutManager = LinearLayoutManager(this)

        findViewById<com.google.android.gms.common.SignInButton>(
            R.id.googleSignInBtn
        ).setOnClickListener { signIn() }

        send.setOnClickListener { sendMessage() }
    }

    // =========================================================
    // 🔐 GOOGLE LOGIN (SAFE)
    // =========================================================

    private fun initGoogleLogin() {

        try {

            val gso = GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
            )
                .requestEmail()
                .build()

            googleSignInClient = GoogleSignIn.getClient(this, gso)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val signInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            try {

                val task = GoogleSignIn
                    .getSignedInAccountFromIntent(result.data)

                val account = task.getResult(ApiException::class.java)

                firebaseAuthWithGoogle(account.idToken ?: return@registerForActivityResult)

            } catch (e: Exception) {
                toast("Login gagal")
            }
        }

    private fun signIn() {
        signInLauncher.launch(googleSignInClient.signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {

        val credential = GoogleAuthProvider.getCredential(idToken, null)

        FirebaseAuth.getInstance()
            .signInWithCredential(credential)
            .addOnCompleteListener {

                if (it.isSuccessful) {

                    loginLayout.visibility = View.GONE
                    chatLayout.visibility = View.VISIBLE

                } else toast("Firebase login gagal")
            }
    }

    // =========================================================
    // 👤 LOGIN CHECK
    // =========================================================

    private fun checkLogin() {

        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {

            loginLayout.visibility = View.GONE
            chatLayout.visibility = View.VISIBLE

        } else {

            loginLayout.visibility = View.VISIBLE
            chatLayout.visibility = View.GONE
            emptyLayout.visibility = View.GONE
        }
    }

    // =========================================================
    // 💬 SEND MESSAGE (SAFE THREAD)
    // =========================================================

    private fun sendMessage() {

        val text = input.text.toString()
        if (text.isBlank()) return

        input.setText("")

        Thread {

            try {

                val response = "AI response placeholder"

                runOnUiThread {
                    toast(response)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread { toast("AI error") }
            }

        }.start()
    }

    // =========================================================
    // 📥 OTA DOWNLOAD (SAFE)
    // =========================================================

    private fun downloadApk(url: String) {

        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle("MyModels Update")
            .setNotificationVisibility(
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
            )

        val dm = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        dm.enqueue(request)
    }

    // =========================================================
    // 🔔 UTIL
    // =========================================================

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}