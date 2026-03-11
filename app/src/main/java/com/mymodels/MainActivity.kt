package com.mymodels

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.mymodels.adapters.ChatAdapter
import com.mymodels.models.ChatMessage
import com.mymodels.services.AIService
import com.mymodels.utils.ModelManager

class MainActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var input: EditText
    private lateinit var send: ImageButton

    private lateinit var loginLayout: View
    private lateinit var chatLayout: View
    private lateinit var emptyLayout: View

    private lateinit var googleSignInClient: GoogleSignInClient

    private val messages = mutableListOf<ChatMessage>()
    private lateinit var adapter: ChatAdapter


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginLayout = findViewById(R.id.loginLayout)
        chatLayout = findViewById(R.id.chatLayout)
        emptyLayout = findViewById(R.id.emptyLayout)

        recycler = findViewById(R.id.chatRecycler)
        input = findViewById(R.id.inputMessage)
        send = findViewById(R.id.btnSend)

        recycler.layoutManager = LinearLayoutManager(this)

        adapter = ChatAdapter(messages)
        recycler.adapter = adapter


        // Google Sign In Setup

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val signInButton =
            findViewById<com.google.android.gms.common.SignInButton>(R.id.googleSignInBtn)

        signInButton.setOnClickListener {

            signIn()

        }


        // Chat send button

        send.setOnClickListener {

            sendMessage()

        }


        // Download model button

        findViewById<Button>(R.id.btnDownloadModel).setOnClickListener {

            startActivity(
                Intent(this, ModelManagerActivity::class.java)
            )

        }


        checkLogin()

    }


    private fun checkLogin() {

        val account = GoogleSignIn.getLastSignedInAccount(this)

        if (account != null) {

            loginLayout.visibility = View.GONE

            checkModel()

        } else {

            loginLayout.visibility = View.VISIBLE
            chatLayout.visibility = View.GONE
            emptyLayout.visibility = View.GONE

        }

    }


    private fun signIn() {

        val signInIntent = googleSignInClient.signInIntent

        startActivityForResult(signInIntent, 1001)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1001) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {

                val account = task.getResult(ApiException::class.java)

                val name = account.displayName

                Toast.makeText(
                    this,
                    "Welcome $name",
                    Toast.LENGTH_LONG
                ).show()

                loginLayout.visibility = View.GONE

                checkModel()

            } catch (e: ApiException) {

                Toast.makeText(
                    this,
                    "Login Failed",
                    Toast.LENGTH_LONG
                ).show()

            }

        }

    }


    private fun checkModel() {

        if (ModelManager.hasModel(this)) {

            chatLayout.visibility = View.VISIBLE
            emptyLayout.visibility = View.GONE

        } else {

            chatLayout.visibility = View.GONE
            emptyLayout.visibility = View.VISIBLE

        }

    }


    private fun sendMessage() {

        val text = input.text.toString()

        if (text.isEmpty()) return

        val userMessage = ChatMessage(text, true)

        messages.add(userMessage)

        adapter.notifyItemInserted(messages.size - 1)

        recycler.scrollToPosition(messages.size - 1)

        input.setText("")

        aiReply(text)

    }


    private fun aiReply(prompt: String) {

        val typing = ChatMessage("...", false)

        messages.add(typing)

        adapter.notifyItemInserted(messages.size - 1)

        recycler.scrollToPosition(messages.size - 1)


        recycler.postDelayed({

            messages.remove(typing)

            val reply = AIService.generate(prompt)

            val aiMessage = ChatMessage(reply, false)

            messages.add(aiMessage)

            adapter.notifyDataSetChanged()

            recycler.scrollToPosition(messages.size - 1)

        }, 1200)

    }

}