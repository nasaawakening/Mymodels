package com.mymodels

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.mymodels.adapters.ChatAdapter
import com.mymodels.models.ChatMessage
import com.mymodels.services.AIService
import com.mymodels.services.ChatRepository
import com.mymodels.utils.ProfileLoader

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatAdapter
    private lateinit var messageInput: EditText
    private lateinit var sendBtn: ImageButton
    private lateinit var googleSignInBtn: SignInButton
    private lateinit var userName: TextView
    private lateinit var userAvatar: ImageView

    private val messages = mutableListOf<ChatMessage>()

    private lateinit var googleSignInClient: GoogleSignInClient

    private val RC_SIGN_IN = 1001

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        messageInput = findViewById(R.id.messageInput)
        sendBtn = findViewById(R.id.sendBtn)
        googleSignInBtn = findViewById(R.id.googleSignInBtn)
        userName = findViewById(R.id.userName)
        userAvatar = findViewById(R.id.userAvatar)

        adapter = ChatAdapter(messages)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        initGoogleLogin()

        loadUser()

        loadChatHistory()

        sendBtn.setOnClickListener {

            sendMessage()

        }

        googleSignInBtn.setOnClickListener {

            signIn()

        }

    }

    private fun initGoogleLogin() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

    }

    private fun signIn() {

        val signInIntent = googleSignInClient.signInIntent

        startActivityForResult(signInIntent, RC_SIGN_IN)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {

                val account = task.getResult(ApiException::class.java)

                firebaseAuthWithGoogle(account.idToken!!)

            } catch (e: ApiException) {

                Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show()

            }

        }

    }

    private fun firebaseAuthWithGoogle(idToken: String) {

        val credential = GoogleAuthProvider.getCredential(idToken, null)

        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {

                    Toast.makeText(this, "Login Success", Toast.LENGTH_LONG).show()

                    loadUser()

                } else {

                    Toast.makeText(this, "Auth Failed", Toast.LENGTH_LONG).show()

                }

            }

    }

    private fun loadUser() {

        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {

            userName.text = user.displayName

            ProfileLoader.loadAvatar(this, user.photoUrl.toString(), userAvatar)

        }

    }

    private fun loadChatHistory() {

        ChatRepository.loadMessages {

            messages.clear()

            messages.addAll(it)

            adapter.notifyDataSetChanged()

            recyclerView.scrollToPosition(messages.size - 1)

        }

    }

    private fun sendMessage() {

        val text = messageInput.text.toString()

        if (text.isEmpty()) return

        val userMessage = ChatMessage("user", text)

        messages.add(userMessage)

        adapter.notifyItemInserted(messages.size - 1)

        recyclerView.scrollToPosition(messages.size - 1)

        messageInput.setText("")

        AIService.sendMessage(text) { response ->

            runOnUiThread {

                val aiMessage = ChatMessage("ai", response)

                messages.add(aiMessage)

                adapter.notifyItemInserted(messages.size - 1)

                recyclerView.scrollToPosition(messages.size - 1)

            }

        }

    }

}