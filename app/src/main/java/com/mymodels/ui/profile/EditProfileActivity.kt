package com.mymodels.ui.profile

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.mymodels.R
import com.mymodels.services.ProfileService

class EditProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val aliasInput = findViewById<EditText>(R.id.aliasInput)
        val saveBtn = findViewById<Button>(R.id.saveAliasBtn)

        saveBtn.setOnClickListener {

            val alias = aliasInput.text.toString()

            ProfileService.saveAlias(alias)

            finish()
        }

    }

}