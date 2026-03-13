package com.mymodels

import android.app.DownloadManager
import android.content.*
import android.content.Intent
import android.net.Uri
import android.os.*
import android.view.View
import android.widget.*

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.File

import com.mymodels.update.UpdateChannel
import com.mymodels.adapters.ChatAdapter
import com.mymodels.models.ChatMessage

import com.mymodels.services.*
import com.mymodels.cloud.ChatCloudService
import com.mymodels.utils.*

import com.mymodels.ui.models.ModelManagerActivity
import com.mymodels.ui.models.TrainModelActivity

// ====== KODE DIPERSINGKAT HEADER IMPORT SAMA ======

class MainActivity : AppCompatActivity() {

    // ===== UI =====

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: ChatAdapter
    private lateinit var input: EditText
    private lateinit var send: ImageButton

    private lateinit var loginLayout: View
    private lateinit var chatLayout: View
    private lateinit var emptyLayout: View

    private lateinit var userName: TextView
    private lateinit var userAvatar: ImageView

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    private lateinit var googleSignInClient: GoogleSignInClient

    private val messages = mutableListOf<ChatMessage>()

    // =========================================================
    // 🚀 ON CREATE
    // =========================================================

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handlePreviousCrash() // 💥 OTA jika crash

        setContentView(R.layout.activity_main)

        initViews()
        initRecycler()
        initGoogleLogin()
        initSidebar()

        checkLogin()
    }

    // =========================================================
    // 💥 CRASH DETECTOR
    // =========================================================

    private fun handlePreviousCrash() {

        val prefs = getSharedPreferences("app", MODE_PRIVATE)
        val crashed = prefs.getBoolean("crashed", false)

        if (crashed) {

        Toast.makeText(
               this,
              "Safe Mode — memperbaiki aplikasi...",
               Toast.LENGTH_LONG
           ).show()

           launchRepairMode()

          prefs.edit().putBoolean("crashed", false).apply()
       }
    }

    // =========================================================
    // 💎 CHANNEL USER
    // =========================================================

    private fun getUserChannel(): UpdateChannel {

        val prefs = getSharedPreferences("app", MODE_PRIVATE)

        val saved = prefs.getString(
            "channel",
            UpdateChannel.STABLE.name
        )

        return UpdateChannel.valueOf(saved!!)
    }

    // =========================================================
    // 🌐 URL OTA
    // =========================================================

    private fun getUpdateUrl(channel: UpdateChannel): String {

        return when (channel) {

            UpdateChannel.STABLE ->
                "https://api.github.com/repos/nasaawakening/Mymodels/releases/latest"

            UpdateChannel.BETA ->
                "https://api.github.com/repos/nasaawakening/Mymodels/releases/tags/beta"

            UpdateChannel.NIGHTLY ->
                "https://api.github.com/repos/nasaawakening/Mymodels/releases/tags/nightly"
        }
    }

    // =========================================================
    // 📡 CHECK OTA
    // =========================================================

    private fun checkForUpdate() {

    Thread {

        try {

            val client = OkHttpClient()

            val request = Request.Builder()
                .url("https://api.github.com/repos/nasaawakening/Mymodels/releases/latest")
                .build()

            val response = client.newCall(request).execute()

            val json = JSONObject(response.body?.string() ?: return@Thread)

            val latest = json.getString("tag_name")

            if (latest != BuildConfig.VERSION_NAME) {

                val assets = json.getJSONArray("assets")

                if (assets.length() > 0) {

                    val apkUrl = assets
                        .getJSONObject(0)
                        .getString("browser_download_url")

                    runOnUiThread { downloadApk(apkUrl) }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }.start()
}

    // =========================================================
    // 📥 DOWNLOAD
    // =========================================================

    private fun downloadApk(url: String) {

        Toast.makeText(this, "Mengunduh update...", Toast.LENGTH_LONG).show()

        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle("MyModels Update")
            .setDescription("Downloading new version")
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                "mymodels_update.apk"
            )

        val dm = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        dm.enqueue(request)

        Handler(Looper.getMainLooper()).postDelayed({
            installApk()
        }, 15000)
    }

    // =========================================================
    // 🔄 INSTALL
    // =========================================================

    private fun installApk() {

        val file = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS
            ),
            "mymodels_update.apk"
        )

        if (!file.exists()) return

        val uri = FileProvider.getUriForFile(
            this,
            "$packageName.provider",
            file
        )

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "application/vnd.android.package-archive")
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

        startActivity(intent)
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

        userName = findViewById(R.id.userName)
        userAvatar = findViewById(R.id.userAvatar)

        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.navView)

        findViewById<com.google.android.gms.common.SignInButton>(
            R.id.googleSignInBtn
        ).setOnClickListener { signIn() }

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

    // =========================================================
    // 🔐 GOOGLE LOGIN
    // =========================================================

    private fun initGoogleLogin() {

        val gso = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN
        )
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun signIn() {
        startActivityForResult(googleSignInClient.signInIntent, 1001)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1001) {

            try {

                val account = GoogleSignIn
                    .getSignedInAccountFromIntent(data)
                    .getResult(ApiException::class.java)

                firebaseAuthWithGoogle(account.idToken!!)

            } catch (e: Exception) {
                toast("Login gagal")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {

        val credential = GoogleAuthProvider.getCredential(idToken, null)

        FirebaseAuth.getInstance()
            .signInWithCredential(credential)
            .addOnCompleteListener {

                if (it.isSuccessful) {

                    toast("Login berhasil")

                    loginLayout.visibility = View.GONE

                    loadUser()
                    checkModel()
                    loadHistory()

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

            loadUser()
            checkModel()
            loadHistory()

        } else {

            loginLayout.visibility = View.VISIBLE
            chatLayout.visibility = View.GONE
            emptyLayout.visibility = View.GONE
        }
    }

    // =========================================================
    // 📂 SIDEBAR
    // =========================================================

    private fun initSidebar() {

        navView.setNavigationItemSelectedListener { item ->

            when (item.itemId) {

                R.id.nav_models ->
                    startActivity(Intent(this, ModelManagerActivity::class.java))

                R.id.nav_train_model ->
                    startActivity(Intent(this, TrainModelActivity::class.java))

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

    // =========================================================
    // 🤖 MODEL CHECK
    // =========================================================

    private fun checkModel() {

        if (ModelManager.hasModel(this)) {

            chatLayout.visibility = View.VISIBLE
            emptyLayout.visibility = View.GONE

        } else {

            chatLayout.visibility = View.GONE
            emptyLayout.visibility = View.VISIBLE
        }
    }

    // =========================================================
    // 👤 PROFILE
    // =========================================================

    private fun loadUser() {

        val user = FirebaseAuth.getInstance().currentUser ?: return

        ProfileService.getAlias { alias ->

            val name = alias ?: user.displayName ?: "User"

            runOnUiThread { userName.text = name }
        }

        ProfileLoader.loadAvatar(userAvatar, user.photoUrl?.toString())
    }

    // =========================================================
    // 💬 HISTORY
    // =========================================================

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

    // =========================================================
    // 💬 SEND
    // =========================================================

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

            val response = AIService.generate(
                userName.text.toString(),
                text
            )

            runOnUiThread {

                val aiMsg = ChatMessage(response, false)

                messages.add(aiMsg)
                ChatCloudService.saveMessage(aiMsg)

                adapter.notifyItemInserted(messages.size - 1)
                recycler.scrollToPosition(messages.size - 1)
            }

        }.start()
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}