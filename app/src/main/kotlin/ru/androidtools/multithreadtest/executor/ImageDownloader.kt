package ru.androidtools.multithreadtest.executor

import android.graphics.Bitmap
import android.widget.ImageView
import com.google.android.material.progressindicator.CircularProgressIndicator
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object ImageDownloader {
    private val executorService: ExecutorService = Executors.newCachedThreadPool()
    private val cache = ConcurrentHashMap<String, Bitmap?>()

    fun download(
        imageUrl: String,
        imageView: ImageView,
        progress: CircularProgressIndicator
    ) {
        if (cache[imageUrl] != null) {
            imageView.setImageBitmap(cache[imageUrl])
            return
        }
        val imageDownloadRunnable = ImageDownloadRunnable(
            WeakReference(imageView),
            WeakReference(progress),
            imageUrl,
            cache
        )
        executorService.submit(imageDownloadRunnable)
    }
}
