package ru.androidtools.multithreadtest.executor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.androidtools.multithreadtest.databinding.ItemImageBinding

class ExecutorImagesAdapter(
    private val images: List<String>,
) : RecyclerView.Adapter<ExecutorImagesAdapter.ImageHolder>() {
    override fun getItemCount(): Int = images.count()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ImageHolder(
        ItemImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(
        holder: ImageHolder,
        position: Int
    ) = holder.bind(images[position])

    class ImageHolder(
        private val binding: ItemImageBinding
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(image: String) = ImageDownloader.download(image, binding.ivImage, binding.progressLoading)
    }
}
