package ru.androidtools.multithreadtest.coroutines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.androidtools.multithreadtest.databinding.FragmentSimpleCoroutinesBinding
import java.util.Date

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SimpleCoroutinesFragment : Fragment() {
    private var _binding: FragmentSimpleCoroutinesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSimpleCoroutinesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCreateCoroutine.setOnClickListener { createCoroutine() }
        binding.btnCreateChannel.setOnClickListener { createChannel() }
        binding.btnCreateFlow.setOnClickListener { createFlow() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /** Создаём корутину */
    private fun createCoroutine() {
        binding.progress.visibility = View.VISIBLE
        binding.tvResult.visibility = View.INVISIBLE

        /* 1 */
//        runBlocking {
//            launch {
//                val result = doWork()
//                binding.progress.visibility = View.INVISIBLE
//                binding.tvResult.visibility = View.VISIBLE
//                binding.tvResult.text = result
//            }
//        }

        /* 2 */
//        GlobalScope.launch {
//            val result = doWork()
//            withContext(Dispatchers.Main) {
//                binding.progress.visibility = View.INVISIBLE
//                binding.tvResult.visibility = View.VISIBLE
//                binding.tvResult.text = result
//            }
//        }

        /* 3 */
//        runBlocking {
//            val coroutine = GlobalScope.async {
//                delay(5000L)
//                "Hello from coroutine ${Thread.currentThread().name}!"
//            }
//
//            binding.progress.visibility = View.INVISIBLE
//            binding.tvResult.visibility = View.VISIBLE
//            binding.tvResult.text = coroutine.await()
//        }

        /* 4 */
        lifecycleScope.launch(CoroutineName("Test coroutine") + Dispatchers.IO) {
            val result = doWork()
            withContext(Dispatchers.Main) {
                binding.progress.visibility = View.INVISIBLE
                binding.tvResult.visibility = View.VISIBLE
                binding.tvResult.text = result
            }
        }

        /* 5 */
//        runBlocking {
//            launch(Dispatchers.IO) {
//                try {
//                    val result = doWork()
//                } catch (e: Exception) {
//                    // Обрабатываем исключение
//                }
//            }
//
//            val deferred = async(Dispatchers.IO) {
//                doWork()
//            }
//
//            try {
//                deferred.await()
//            } catch (e: Exception) {
//                // Обрабатываем исключение
//            }
//        }

        /* 6 */
//        runBlocking {
//            launch(Dispatchers.IO) {
//                try {
//                    val result = doWork()
//                } catch (e: Exception) {
//                    // Обрабатываем исключение
//                } catch (e: CancellationException) {
//                    // Обрабатываем и пробрасываем дальше
//                    throw e
//                }
//            }
//        }
    }

    /** Создаём каналы */
    private fun createChannel() {
        binding.progress.visibility = View.VISIBLE
        binding.tvResult.visibility = View.INVISIBLE

        val channel = Channel<String>()

        lifecycleScope.launch(Dispatchers.IO) {
            val message = "Hello from coroutine ${Thread.currentThread().name}!"
            channel.send(message)
        }

        lifecycleScope.launch(Dispatchers.IO) {
            delay(3000L)
            val message = channel.receive()
            withContext(Dispatchers.Main) {
                binding.progress.visibility = View.INVISIBLE
                binding.tvResult.visibility = View.VISIBLE
                binding.tvResult.text = message
                channel.close()
            }
        }
    }

    /** Создаём поток данных */
    private fun createFlow() {
        binding.tvResult.visibility = View.VISIBLE

        lifecycleScope.launch(Dispatchers.IO) {
            flow {
                while (true) {
                    delay(1000L)
                    emit(Date())
                }
            }.collect { date ->
                withContext(Dispatchers.Main) {
                    binding.tvResult.text = date.toString()
                }
            }
        }
    }

    private suspend fun doWork(): String {
        delay(5000L)
        return "Hello from coroutine ${Thread.currentThread().name}!"
    }
}
