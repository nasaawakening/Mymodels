package com.mymodels.utils

object SizeFilter {

    fun valid(sizeBytes:Long):Boolean{

        val gb=sizeBytes/(1024*1024*1024)

        return gb in 1..3
    }
}
