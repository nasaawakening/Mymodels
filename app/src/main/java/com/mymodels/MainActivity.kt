package com.mymodels

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.mymodels.adapters.ChatAdapter
import com.mymodels.models.ChatMessage
import com.mymodels.services.AIService
import com.mymodels.services.ProfileService
import com.mymodels.cloud.ChatCloudService
import com.mymodels.services.ChatRepository
import com.mymodels.utils.ModelManager
import com.mymodels.utils.ProfileLoader
import com.mymodels.ui.models.ModelManagerActivity
import com.mymodels.ui.models.TrainModelActivity

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

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    private val messages = mutableListOf<ChatMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        initViews()

        initRecycler()

        initGoogleLogin()

        initSidebar()

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

        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.navView)

        findViewById<com.google.android.gms.common.SignInButton>(R.id.googleSignInBtn)
            .setOnClickListener { signIn() }

        findViewById<Button>(R.id.btnDownloadModel)
            .setOnClickListener {
                startActivity(Intent(this, ModelManagerActivity::class.java))
            }

        send.setOnClickListener { sendMessage() }
    }

    private fun initRecycler() {

        adapter = ChatAdapter(messages)

        recycler.layoutManager = LinearLayoutManager(this)

        recycler.adapter = adapter
    }

    private fun initGoogleLogin() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun initSidebar() {

        navView.setNavigationItemSelectedListener { item ->

            when (item.itemId) {

                R.id.nav_models -> {
                    startActivity(Intent(this, ModelManagerActivity::class.java))
                }

                R.id.nav_train_model -> {
                    startActivity(Intent(this, TrainModelActivity::class.java))
                }

                R.id.nav_logout -> {

                    FirebaseAuth.getInstance().signOut()

                    googleSignInClient.signOut()

                    recreate()
                }
            }

            drawerLayout.closeDrawers()

            true
        }
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

                val account = task.getResult(ApiException::class.java)

                firebaseAuthWithGoogle(account.idToken!!)

            } catch (e: Exception) {

                Toast.makeText(this, "Login gagal", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {

        val credential = GoogleAuthProvider.getCredential(idToken, null)

        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener {

                if (it.isSuccessful) {

                    Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()

                    loginLayout.visibility = View.GONE

                    loadUser()

                    checkModel()

                    loadHistory()

                } else {

                    Toast.makeText(this, "Firebase login gagal", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun checkLogin() {

        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {

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

        ProfileService.getAlias { alias ->

            val name = alias ?: user.displayName ?: "User"

            runOnUiThread {
                userName.text = name
            }
        }

        ProfileLoader.loadAvatar(userAvatar, user.photoUrl?.toString())
    }

    private fun loadHistory() {

        ChatRepository.loadMessages {

            runOnUiThread {

                messages.clear()

                messages.addAll(it)

                adapter.notifyDataSetChanged()

                if (messages.isNotEmpty())
                    recycler.scrollToPosition(messages.size - 1)
            }
        }
    }

    private fun sendMessage() {

        val text = input.text.toString()

        if (text.isEmpty()) return

        val userMsg = ChatMessage(text, true)

        messages.add(userMsg)

        ChatCloudService.saveMessage(userMsg)

        adapter.notifyItemInserted(messages.size - 1)

        recycler.scrollToPosition(messages.size - 1)

        input.setText("")

        Thread {

            val name = userName.text.toString()

            val response = AIService.generate(name, text)

            runOnUiThread {

                val aiMsg = ChatMessage(response, false)

                messages.add(aiMsg)

                ChatCloudService.saveMessage(aiMsg)

                adapter.notifyItemInserted(messages.size - 1)

                recycler.scrollToPosition(messages.size - 1)
            }

        }.start()
    }
}