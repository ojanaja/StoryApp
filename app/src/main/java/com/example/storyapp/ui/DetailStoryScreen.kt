package com.example.storyapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.data.Story
import com.example.storyapp.databinding.ActivityDetailStoryScreenBinding

class DetailStoryScreen : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()
        fetchData()
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            title = getString(R.string.details)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun fetchData() {
        intent.getParcelableExtra<Story>(EXTRA_ITEM)?.let { story ->
            binding.apply {
                tvName.text = story.name
                tvDescription.text = story.description
                Glide.with(this@DetailStoryScreen)
                    .load(story.photoUrl)
                    .into(imgPhotos)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    companion object {
        const val EXTRA_ITEM = "extra_item"
    }
}
