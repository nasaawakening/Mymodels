package com.mymodels.ui.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mymodels.ai.engine.GGUFRunner

class ChatActivity:AppCompatActivity(){

    private val runner=GGUFRunner()

    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
    }

    fun ask(prompt:String):String{

        return runner.prompt(prompt)
    }
}
