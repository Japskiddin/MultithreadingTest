package ru.androidtools.multithreadtest

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.androidtools.multithreadtest.databinding.ItemImageBinding

class ImageHandlerThreadAdapter(
    private val images: List<String>,
    private val imageHandlerThread: ImageHandlerThread<Int>
) : RecyclerView.Adapter<ImageHandlerThreadAdapter.ImageHolder>() {
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
    ) = super.onBindViewHolder(holder, position, emptyList())

    override fun onBindViewHolder(
        holder: ImageHolder,
        position: Int,
        payloads: List<Any>
    ) {
        if (payloads.isNotEmpty()) {
            for (payload in payloads) {
                if (payload is ImagePayload) {
                    holder.updateImage(payload.image)
                }
            }
        } else {
            val image = images[position]
            holder.bind()
            imageHandlerThread.queueImage(position, image)
        }
    }

    fun updateItem(position: Int, image: Bitmap) {
        val imagePayload = ImagePayload(image)
        notifyItemChanged(position, imagePayload)
    }

    class ImageHolder(
        private val binding: ItemImageBinding
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind() = showProgress()

        fun updateImage(image: Bitmap) {
            hideProgress()
            binding.ivImage.setImageBitmap(image)
        }

        private fun showProgress() {
            binding.ivImage.visibility = View.INVISIBLE
            binding.progressLoading.visibility = View.VISIBLE
        }

        private fun hideProgress() {
            binding.ivImage.visibility = View.VISIBLE
            binding.progressLoading.visibility = View.GONE
        }
    }

    private data class ImagePayload(val image: Bitmap)
}
