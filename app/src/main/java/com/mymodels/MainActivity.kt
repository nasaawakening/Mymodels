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
import com.google.firebase.auth.FirebaseAuth
import com.mymodels.cloud.ChatCloudService
import com.mymodels.adapters.ChatAdapter
import com.mymodels.models.ChatMessage
import com.mymodels.services.AIService
import com.mymodels.services.ChatRepository
import com.mymodels.utils.ModelManager
import com.mymodels.utils.ProfileLoader

class MainActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: ChatAdapter
    private lateinit var input: EditText
    private lateinit var send: ImageButton

    private lateinit var loginLayout: View
    private lateinit var chatLayout: View
    private lateinit var emptyLayout: View

    private lateinit var userName: TextView
    private lateinit var userAvatar: ImageView

    private lateinit var googleSignInClient: GoogleSignInClient

    private val messages = mutableListOf<ChatMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        initViews()

        initRecycler()

        initGoogleLogin()

        checkLogin()

    }

    private fun initViews() {

        loginLayout = findViewById(R.id.loginLayout)
        chatLayout = findViewById(R.id.chatLayout)
        emptyLayout = findViewById(R.id.emptyLayout)

        recycler = findViewById(R.id.chatRecycler)
        input = findViewById(R.id.inputMessage)
        send = findViewById(R.id.btnSend)

        userName = findViewById(R.id.userName)
        userAvatar = findViewById(R.id.userAvatar)

        findViewById<com.google.android.gms.common.SignInButton>(R.id.googleSignInBtn)
            .setOnClickListener { signIn() }

        findViewById<Button>(R.id.btnDownloadModel)
            .setOnClickListener {

                startActivity(Intent(this, ModelManagerActivity::class.java))

            }

        send.setOnClickListener {

            sendMessage()

        }

    }

    private fun initRecycler() {

        adapter = ChatAdapter(messages)

        recycler.layoutManager = LinearLayoutManager(this)

        recycler.adapter = adapter

    }

    private fun initGoogleLogin() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

    }

    private fun signIn() {

        val intent = googleSignInClient.signInIntent

        startActivityForResult(intent, 1001)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1001) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {

                task.getResult(ApiException::class.java)

                Toast.makeText(this, "Login success", Toast.LENGTH_SHORT).show()

                checkModel()

                loadUser()

                loadHistory()

            } catch (e: ApiException) {

                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()

            }

            ProfileService.getAlias { alias ->

              val googleName =
                com.google.firebase.auth.FirebaseAuth.getInstance()
                .currentUser?.displayName ?: "User"

               val name = alias ?: googleName

               val response =
                 AIService.generate(name, prompt)

           }

        }

    }

    private fun checkLogin() {

        val account = GoogleSignIn.getLastSignedInAccount(this)

        if (account != null) {

            loginLayout.visibility = View.GONE

            loadUser()

            checkModel()

            loadHistory()

        } else {

            loginLayout.visibility = View.VISIBLE

            chatLayout.visibility = View.GONE

            emptyLayout.visibility = View.GONE

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

    private fun loadUser() {

        val user = FirebaseAuth.getInstance().currentUser ?: return

        userName.text = user.displayName

        ProfileLoader.loadAvatar(this, user.photoUrl.toString(), userAvatar)

    }

    private fun loadHistory() {

        ChatRepository.loadMessages {

            messages.clear()

            messages.addAll(it)

            adapter.notifyDataSetChanged()

            if (messages.isNotEmpty())
                recycler.scrollToPosition(messages.size - 1)

        }

    }

    private fun loadAvatar(url: String?) {
    // sementara kosong dulu
   }

    private fun sendMessage() {

        val text = input.text.toString()

        if (text.isEmpty()) return

        val userMsg = ChatMessage("user", text)

        messages.add(userMsg)

        adapter.notifyItemInserted(messages.size - 1)

        recycler.scrollToPosition(messages.size - 1)

        input.setText("")

        val typing = ChatMessage("ai", "...")

        messages.add(typing)

        ChatCloudService.saveMessage(userMessage)

        adapter.notifyItemInserted(messages.size - 1)

        recycler.scrollToPosition(messages.size - 1)

        AIService.sendMessage(text) { response ->

            runOnUiThread {

                messages.remove(typing)

                val aiMsg = ChatMessage("ai", response)

                ChatCloudService.saveMessage(aiMessage)

                messages.add(aiMsg)

                adapter.notifyDataSetChanged()

                recycler.scrollToPosition(messages.size - 1)

            }

        }

    }

}