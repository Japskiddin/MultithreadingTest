package ru.androidtools.multithreadtest

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.androidtools.multithreadtest.databinding.FragmentHandlerThreadBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class HandlerThreadFragment : Fragment() {
    private var _binding: FragmentHandlerThreadBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val imageHandlerThread: ImageHandlerThread<Int> = ImageHandlerThread(
        Handler(Looper.getMainLooper()),
        object : ImageHandlerThread.ThreadListener<Int> {
            override fun onImageDownloaded(target: Int, image: Bitmap) {
                imagesAdapter.updateItem(target, image)
            }
        }
    )
    private val imagesAdapter: ImagesAdapter = ImagesAdapter(IMAGES_LIST, imageHandlerThread)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        imageHandlerThread.start()
        _binding = FragmentHandlerThreadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvImages.adapter = imagesAdapter
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        imageHandlerThread.clearQueue()
        imageHandlerThread.quit()
        _binding = null
    }

    companion object {
        val IMAGES_LIST = listOf(
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTFJFlv82Ka95K2YdwY-ysJJWu2S7BanGhVXw&s",
            "https://bestfriends.org/sites/default/files/2023-04/JerryArlyneBenFrechette2915sak.jpg",
            "https://www.cats.org.uk/media/12883/210908ncac104.jpg?width=500&height=333.30078125",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR4ZBVltbIEEDTKwVGA2fRX3wW7rT4tR3k_Kw&s",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTcPrFmN5loBnmv5CEWJ6PtBzhrAekTRh7w0Q&s",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT7r0kCHWP3ZrXtB8U8sfTWoE3YXy__m4_nzg&s",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQX63kZF6VlsxTiLH5cBYFMn00zBd-2x7OsbQ&s",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT5H3p6Ir1LHathVgxS96fQbXK0-twVSNdFwQ&s",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTIs-9IrXb1_0htSOE1hUcONujC0CoFeVOctg&s",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTd3FsMmvPJ3IcUOgDrBCMvsJYcSo9UeUbpMQ&s",
            "https://image.petmd.com/files/petmd-kitten-facts.jpg",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQBi2UnpivXXhTW6uKH2R67lryUl3j2WklzOw&s"
        )
    }
}
