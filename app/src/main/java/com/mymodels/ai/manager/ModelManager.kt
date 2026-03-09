package com.mymodels.ai.manager

import java.io.File

class ModelManager(private val dir:File){

    fun listModels():List<File>{

        return dir.listFiles()?.toList() ?: emptyList()
    }

    fun getModel(name:String):File{

        return File(dir,name)
    }
}
