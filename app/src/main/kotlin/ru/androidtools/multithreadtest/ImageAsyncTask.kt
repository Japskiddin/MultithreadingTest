package ru.androidtools.multithreadtest

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.annotation.WorkerThread
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class ImageAsyncTask(
    private val taskListener: TaskListener
) : AsyncTask<String, Int, Bitmap>() {
    interface TaskListener {
        fun onTaskStarted()

        fun onProgressUpdated(progress: Int)

        fun onTaskCompleted(bitmap: Bitmap?)
    }

    override fun onPreExecute() {
        taskListener.onTaskStarted()
    }

    override fun doInBackground(vararg strings: String): Bitmap? {
        publishProgress(0)
        val bitmap = loadImage(strings[0])
        publishProgress(100)
        return bitmap
    }

    override fun onProgressUpdate(vararg values: Int?) {
        if (values.isEmpty()) return
        val progress = values[0]
        if (progress != null) {
            taskListener.onProgressUpdated(progress)
        }
    }

    override fun onPostExecute(result: Bitmap?) {
        taskListener.onTaskCompleted(result)
    }

    @WorkerThread
    private fun loadImage(link: String): Bitmap? {
        val url = URL(link)
        var connection: HttpURLConnection? = null
        var inputStream: BufferedInputStream? = null
        val buffer = ByteArrayOutputStream()
        return try {
            connection = url.openConnection() as HttpURLConnection
            connection.connect()
            inputStream = BufferedInputStream(connection.inputStream)
            val totalSize = connection.contentLength
            val bufferSize = 512
            val tempBuffer = ByteArray(bufferSize)
            var bytesRead: Int
            var downloadedSize: Int = 0
            while (inputStream.read(tempBuffer, 0, bufferSize).also { bytesRead = it } != -1) {
                buffer.write(tempBuffer, 0, bytesRead)
                downloadedSize += bytesRead
                if (totalSize > 0) {
                    publishProgress((downloadedSize * 100) / totalSize)
                }
            }
            val array = buffer.toByteArray()
            BitmapFactory.decodeByteArray(array, 0, array.size)
        } catch (e: IOException) {
            null
        } finally {
            connection?.disconnect()
            inputStream?.close()
            buffer.flush()
            buffer.close()
        }
    }
}
