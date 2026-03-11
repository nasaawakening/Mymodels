package com.mymodels.services

import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

object ModelDownloader {

    private val activeDownloads = mutableMapOf<String, Boolean>()

    fun download(
        url: String,
        file: File,
        onProgress: ((Int) -> Unit)? = null,
        onComplete: (() -> Unit)? = null
    ) {

        activeDownloads[url] = true

        thread {

            try {

                val connection = URL(url).openConnection() as HttpURLConnection
                connection.connect()

                val totalSize = connection.contentLength

                val input = connection.inputStream
                val output = FileOutputStream(file)

                val buffer = ByteArray(4096)

                var downloaded = 0

                while (true) {

                    if (activeDownloads[url] == false) break

                    val read = input.read(buffer)

                    if (read == -1) break

                    output.write(buffer, 0, read)

                    downloaded += read

                    if (totalSize > 0) {

                        val progress = (downloaded * 100) / totalSize
                        onProgress?.invoke(progress)

                    }

                }

                input.close()
                output.close()

                if (activeDownloads[url] == true) {
                    onComplete?.invoke()
                }

                activeDownloads.remove(url)

            } catch (e: Exception) {

                e.printStackTrace()

            }

        }

    }

    fun cancel(url: String) {

        activeDownloads[url] = false

    }

    fun getModelSize(url: String): Long {

        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "HEAD"
        connection.connect()

        return connection.contentLengthLong

    }

}