package ru.androidtools.multithreadtest

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.androidtools.multithreadtest.databinding.FragmentSimpleAsyncTaskBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SimpleAsyncTaskFragment : Fragment() {
    private var _binding: FragmentSimpleAsyncTaskBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSimpleAsyncTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnStart.setOnClickListener { createAsyncTask() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Создаём AsyncTask
     */
    private fun createAsyncTask() {
        class MyAsyncTask : AsyncTask<Void, Int, Int>() {
            override fun onPreExecute() {
                Toast.makeText(requireContext(), "Task started", Toast.LENGTH_SHORT).show()
            }

            override fun doInBackground(vararg values: Void?): Int {
                for (i in 1 until 10) {
                    publishProgress(i)
                    Thread.sleep(1000L)
                }
                return 10
            }

            override fun onProgressUpdate(vararg values: Int?) {
                val value = values[0]
                if (value != null) {
                    binding.progress.setProgress(value)
                }
            }

            override fun onPostExecute(result: Int) {
                Toast.makeText(requireContext(), "Task completed", Toast.LENGTH_SHORT).show()
                binding.progress.setProgress(result)
            }
        }

        val asyncTask = MyAsyncTask()
        asyncTask.execute()
    }
}
