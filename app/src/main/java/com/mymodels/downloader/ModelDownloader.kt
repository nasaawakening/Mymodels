package com.mymodels.downloader

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File

class ModelDownloader {

    private val client=OkHttpClient()

    fun download(url:String, file:File){

        val request=Request.Builder()
            .url(url)
            .build()

        NotificationHelper.show(
        context,
        "Download selesai",
        "Model berhasil didownload")

        val response=client.newCall(request).execute()

        file.outputStream().use{
            response.body!!.byteStream().copyTo(it)
        }
    }
}
