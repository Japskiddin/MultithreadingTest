package ru.androidtools.multithreadtest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.androidtools.multithreadtest.databinding.FragmentHomeBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSimpleThreads.setOnClickListener {
            findNavController().navigate(R.id.action_HomeFragment_to_SimpleThreadFragment)
        }
        binding.btnHandlerThread.setOnClickListener {
            findNavController().navigate(R.id.action_HomeFragment_to_HandlerThreadFragment)
        }
        binding.btnSimpleAsynctask.setOnClickListener {
            findNavController().navigate(R.id.action_HomeFragment_to_SimpleAsyncTaskFragment)
        }
        binding.btnImageAsynctask.setOnClickListener {
            findNavController().navigate(R.id.action_HomeFragment_to_ImageAsyncTaskFragment)
        }
        binding.btnSimpleLoader.setOnClickListener {
            findNavController().navigate(R.id.action_HomeFragment_to_SimpleLoaderFragment)
        }
        binding.btnExecutor.setOnClickListener {
            findNavController().navigate(R.id.action_HomeFragment_to_ExecutorFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
