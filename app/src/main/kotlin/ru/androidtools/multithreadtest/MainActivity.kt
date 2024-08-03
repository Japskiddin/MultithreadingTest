package ru.androidtools.multithreadtest

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.lang.Thread.currentThread
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // createThread()
        createThreadWithLooper()
    }

    /**
     * Создаём простые потоки
     */
    private fun createThread() {
        /* Первый способ */
        // class MyThread : Thread() {
        //   override fun run() {
        //     // Код
        //     sleep(5000)
        //     runOnUiThread {
        //       Toast.makeText(this@MainActivity, "Hello from $name", Toast.LENGTH_SHORT).show()
        //     }
        //   }
        // }
        //
        // val firstThread = MyThread()
        // firstThread.start()

        /* Второй способ */
        // class MyRunnable : Runnable {
        //   override fun run() {
        //     // Код
        //     Thread.sleep(5000)
        //     runOnUiThread {
        //       Toast.makeText(
        //         this@MainActivity,
        //         "Hello from ${Thread.currentThread().name}",
        //         Toast.LENGTH_SHORT
        //       ).show()
        //     }
        //   }
        // }
        //
        // val secondThread = Thread(MyRunnable())
        // secondThread.start()

        /* Третий способ */
        // val thread: Thread = object : Thread() {
        //   override fun run() {
        //     sleep(5000)
        //     runOnUiThread {
        //       Toast.makeText(this@MainActivity, "Hello from $name", Toast.LENGTH_SHORT).show()
        //     }
        //   }
        // }
        // thread.name = "Test Thread"
        // thread.start()

        /* Четвёртый способ */
        thread(name = "Test Thread") {
            Thread.sleep(5000)
            val name = Thread.currentThread().name
            runOnUiThread {
                Toast.makeText(this, "Hello from $name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createThreadWithLooper() {
        class LooperThread : Thread() {
            lateinit var handler: Handler

            override fun run() {
                val threadName = name
                Looper.prepare()
                handler = Handler(Looper.myLooper()!!) { message ->
                    val messageName = message.obj as String
                    runOnUiThread {
                        Toast.makeText(
                            this@MainActivity,
                            "$threadName send hello from $messageName",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    true
                }
                Looper.loop()
            }
        }

        val looperThread = LooperThread()
        looperThread.name = "Test Thread 1"
        looperThread.start()

        thread(name = "Test Thread 2") {
            Thread.sleep(3000)
            val message = Message.obtain().apply {
                obj = currentThread().name
            }
            looperThread.handler.sendMessage(message)
        }
    }
}
