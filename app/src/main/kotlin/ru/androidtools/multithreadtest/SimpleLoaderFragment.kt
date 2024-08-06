package ru.androidtools.multithreadtest

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import ru.androidtools.multithreadtest.databinding.FragmentSimpleLoaderBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SimpleLoaderFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {
    private var _binding: FragmentSimpleLoaderBinding? = null
    private lateinit var db: TestDatabase

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSimpleLoaderBinding.inflate(inflater, container, false)
        db = TestDatabase(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db.open()
        binding.progress.visibility = View.INVISIBLE
        binding.result.visibility = View.INVISIBLE
        binding.btnStart.visibility = View.VISIBLE
        binding.btnStart.setOnClickListener { createLoader() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        db.close()
        _binding = null
    }

    /**
     * Создаём Loader
     */
    private fun createLoader() {
        binding.btnStart.visibility = View.INVISIBLE
        requireActivity().supportLoaderManager.initLoader(LOADER_ID, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        binding.progress.visibility = View.VISIBLE
        return MyCursorLoader(requireContext(), db)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        binding.progress.visibility = View.INVISIBLE
        binding.result.visibility = View.VISIBLE
        data?.let { cursor ->
            if (cursor.count > 0) {
                val sb = StringBuilder().apply {
                    while (cursor.moveToNext()) {
                        val columnIndex = cursor.getColumnIndex(TestDatabase.COLUMN_TXT)
                        append(cursor.getString(columnIndex))
                        append("\n")
                    }
                }
                binding.result.text = sb.toString()
            }
        }
    }

    private class MyCursorLoader(context: Context, private val db: TestDatabase) : CursorLoader(context) {
        override fun loadInBackground(): Cursor? {
            Thread.sleep(1000)
            return db.getAllData()
        }
    }

    private companion object {
        const val LOADER_ID = 101
    }
}
