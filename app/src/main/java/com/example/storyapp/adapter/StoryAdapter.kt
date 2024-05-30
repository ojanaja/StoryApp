package com.example.storyapp.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.storyapp.R
import com.example.storyapp.data.Story
import com.example.storyapp.databinding.ItemStoryLayoutBinding
import com.example.storyapp.ui.DetailStoryScreen

class StoryAdapter : PagingDataAdapter<Story, StoryAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(private val binding: ItemStoryLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, story: Story) {
            with(binding) {
                tvName.text = story.name
                tvDescription.text = story.description

                val options = RequestOptions()
                    .placeholder(R.drawable.ic_image)
                    .error(R.drawable.ic_error)

                Glide.with(context).load(story.photoUrl).apply(options).into(imgPhotos)

                root.setOnClickListener {
                    val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        context as Activity,
                        Pair(imgPhotos, "image"),
                        Pair(tvName, "name"),
                        Pair(tvDescription, "description")
                    )

                    Intent(context, DetailStoryScreen::class.java).apply {
                        putExtra(DetailStoryScreen.EXTRA_ITEM, story)
                        context.startActivity(this, optionsCompat.toBundle())
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { story ->
            holder.bind(holder.itemView.context, story)
        }
    }

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }
        }
    }
}
