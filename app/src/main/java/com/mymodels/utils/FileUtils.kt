package com.mymodels.utils

import java.io.File

object FileUtils {

    fun modelsDir(base:File):File{

        val dir=File(base,"models")

        if(!dir.exists())
            dir.mkdirs()

        return dir
    }

    fun formatSize(size:Long):String{

        val kb=size/1024
        val mb=kb/1024
        val gb=mb/1024

        return when{
            gb>0 -> "${gb}GB"
            mb>0 -> "${mb}MB"
            else -> "${kb}KB"
        }
    }
}
