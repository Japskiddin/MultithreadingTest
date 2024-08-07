package ru.androidtools.multithreadtest.thread

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.androidtools.multithreadtest.databinding.FragmentSimpleThreadBinding
import java.lang.Thread.currentThread
import kotlin.concurrent.thread

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SimpleThreadFragment : Fragment() {
    private var _binding: FragmentSimpleThreadBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSimpleThreadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCreateThread.setOnClickListener { createThread() }
        binding.btnCreateLooper.setOnClickListener { createThreadWithLooper() }
        binding.btnCreateHandlerThread.setOnClickListener { createHandlerThread() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
            val name = currentThread().name
            activity?.runOnUiThread {
                Toast.makeText(requireContext(), "Hello from $name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Создаём поток с подключенным Looper
     */
    private fun createThreadWithLooper() {
        class LooperThread : Thread() {
            lateinit var handler: Handler

            override fun run() {
                val threadName = name
                Looper.prepare()
                handler = Handler(Looper.myLooper()!!) { message ->
                    val messageName = message.obj as String
                    activity?.runOnUiThread {
                        Toast.makeText(
                            requireContext(),
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

    /**
     * Создаём HandlerThread
     */
    private fun createHandlerThread() {
        val handlerThread = HandlerThread("Test Handler Thread")
        handlerThread.start()
        val handler = Handler(handlerThread.looper) { message ->
            val messageName = message.obj as String
            Toast.makeText(
                requireContext(),
                "${currentThread().name} send hello from $messageName",
                Toast.LENGTH_SHORT
            ).show()
            true
        }

        thread(name = "Test Thread 2") {
            Thread.sleep(3000)
            val message = Message.obtain().apply {
                obj = currentThread().name
            }
            handler.sendMessage(message)
        }
    }
}
