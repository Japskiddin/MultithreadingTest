package ru.androidtools.multithreadtest.executor

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import com.google.android.material.progressindicator.CircularProgressIndicator
import java.io.BufferedInputStream
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ConcurrentHashMap

class ImageDownloadRunnable(
    private val imageViewRef: WeakReference<ImageView>,
    private val progressRef: WeakReference<CircularProgressIndicator>,
    private val imageUrl: String,
    private val cache: ConcurrentHashMap<String, Bitmap?>
) : Runnable {
    override fun run() {
        progressRef.get()?.let { progress ->
            progress.post { progress.visibility = View.VISIBLE }
        }
        imageViewRef.get()?.let { target ->
            target.post { target.visibility = View.INVISIBLE }
        }

        val bitmap = loadImage(imageUrl).also {
            if (it != null) {
                cache[imageUrl] = it
            }
        }

        progressRef.get()?.let { progress ->
            progress.post { progress.visibility = View.INVISIBLE }
        }

        if (bitmap != null) {
            imageViewRef.get()?.let { target ->
                target.post {
                    target.visibility = View.VISIBLE
                    target.setImageBitmap(bitmap)
                }
            }
        }
    }

    private fun loadImage(imageUrl: String): Bitmap? {
        val url = URL(imageUrl)
        var connection: HttpURLConnection? = null
        return try {
            connection = url.openConnection() as HttpURLConnection
            connection.connect()
            BufferedInputStream(connection.inputStream).use { `is` ->
                BitmapFactory.decodeStream(`is`)
            }
        } catch (e: IOException) {
            null
        } finally {
            connection?.disconnect()
        }
    }
}
