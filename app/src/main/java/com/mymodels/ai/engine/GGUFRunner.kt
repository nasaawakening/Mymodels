package com.mymodels.ai.engine

class GGUFRunner {

    external fun loadModel(path:String)

    external fun prompt(text:String):String

    companion object{

        init{
            System.loadLibrary("llama")
        }
    }
}
