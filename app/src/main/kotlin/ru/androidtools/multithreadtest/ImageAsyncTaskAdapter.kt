package ru.androidtools.multithreadtest

import android.app.Activity
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.androidtools.multithreadtest.databinding.ItemImageBinding

class ImageAsyncTaskAdapter(
    private val images: List<String>,
) : RecyclerView.Adapter<ImageAsyncTaskAdapter.ImageHolder>() {
    /* Для второго способа */
//    private val executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

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
    /* Для второго способа */
//    ) = holder.bind(images[position], executorService)

    class ImageHolder(
        private val binding: ItemImageBinding
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(image: String) {
            /* Для второго способа */
//        fun bind(image: String, executorService: ExecutorService) {
            binding.progressLoading.isIndeterminate = false
            binding.progressLoading.max = 100
            val asyncTask = ImageAsyncTask(object : ImageAsyncTask.TaskListener {
                override fun onTaskStarted() {
                    showProgress()
                }

                override fun onProgressUpdated(progress: Int) {
                    (itemView.context as Activity).runOnUiThread {
                        binding.progressLoading.progress = progress
                    }
                }

                override fun onTaskCompleted(bitmap: Bitmap?) {
                    hideProgress()
                    binding.ivImage.setImageBitmap(bitmap)
                }
            })
            asyncTask.execute(image)

            /* Для второго способа */
//            asyncTask.executeOnExecutor(executorService, image)
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
}
