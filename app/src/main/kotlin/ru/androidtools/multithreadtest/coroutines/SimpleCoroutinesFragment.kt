package ru.androidtools.multithreadtest.coroutines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.androidtools.multithreadtest.databinding.FragmentSimpleCoroutinesBinding

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /** Создаём корутину */
    private fun createCoroutine() {

    }
}
