package ru.androidtools.multithreadtest.loader

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import ru.androidtools.multithreadtest.databinding.FragmentSimpleLoaderBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SimpleLoaderFragment : Fragment(), LoaderManager.LoaderCallbacks<String> {
    private var _binding: FragmentSimpleLoaderBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSimpleLoaderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progress.visibility = View.INVISIBLE
        binding.result.visibility = View.INVISIBLE
        binding.btnStart.visibility = View.VISIBLE
        binding.btnStart.setOnClickListener { createLoader() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Создаём Loader
     */
    private fun createLoader() {
        binding.btnStart.visibility = View.INVISIBLE
        requireActivity().supportLoaderManager.initLoader(LOADER_ID, null, this).forceLoad()
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<String> {
        binding.progress.visibility = View.VISIBLE
        return MyCustomLoader(requireContext())
    }

    override fun onLoaderReset(loader: Loader<String>) {
    }

    override fun onLoadFinished(loader: Loader<String>, data: String) {
        binding.progress.visibility = View.INVISIBLE
        binding.result.visibility = View.VISIBLE
        binding.result.text = data
    }

    private class MyCustomLoader(
        context: Context
    ) : AsyncTaskLoader<String>(context) {
        override fun loadInBackground(): String {
            val db = TestDatabase(context)
            db.open()
            Thread.sleep(3000)
            val cursor = db.getAllData()
            val sb = StringBuilder().apply {
                if (cursor != null && cursor.count > 0) {
                    while (cursor.moveToNext()) {
                        val columnIndex = cursor.getColumnIndex(TestDatabase.COLUMN_TXT)
                        append(cursor.getString(columnIndex))
                        append("\n")
                    }
                }
            }
            db.close()
            return sb.toString()
        }
    }

    private companion object {
        const val LOADER_ID = 101
    }
}
