package ru.androidtools.multithreadtest

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.HandlerThread
import java.io.BufferedInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ConcurrentHashMap


class ImageHandlerThread<T : Any>(
    private val responseHandler: Handler,
    private val listener: ThreadListener<T>
) : HandlerThread("Images Handler Thread") {
    interface ThreadListener<T> {
        fun onImageDownloaded(target: T, image: Bitmap)
    }

    private lateinit var requestHandler: Handler
    private val requestMap: ConcurrentHashMap<T, String> = ConcurrentHashMap()

    override fun onLooperPrepared() {
        requestHandler = Handler(looper) { message ->
            if (message.what == MESSAGE_DOWNLOAD) {
                @Suppress("UNCHECKED_CAST")
                val target = message.obj as T
                handleRequest(target)
                true
            } else {
                false
            }
        }
    }

    fun queueImage(target: T, link: String) {
        requestMap[target] = link
        requestHandler.obtainMessage(MESSAGE_DOWNLOAD, target).sendToTarget()
    }

    fun clearQueue() {
        requestHandler.removeMessages(MESSAGE_DOWNLOAD)
        requestMap.clear()
    }

    private fun handleRequest(target: T) {
        val link = requestMap[target] ?: return
        val bitmap = loadImage(link) ?: return
        responseHandler.post {
            val result = requestMap[target]
            if (result == null || result != link) return@post
            requestMap.remove(target)
            listener.onImageDownloaded(target, bitmap)
        }
    }

    private fun loadImage(link: String): Bitmap? {
        val url = URL(link)
        var connection: HttpURLConnection? = null
        return try {
            connection = url.openConnection() as HttpURLConnection
            connection.connect()
            val bufferedInputStream = BufferedInputStream(connection.inputStream)
            BitmapFactory.decodeStream(bufferedInputStream)
        } catch (e: IOException) {
            null
        } finally {
            connection?.disconnect()
        }
    }

    companion object {
        const val MESSAGE_DOWNLOAD = 101
    }
}
