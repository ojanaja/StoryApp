package com.example.storyapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.storyapp.databinding.LoadingStateBinding

class LoadingState : LoadStateAdapter<LoadingState.LoadingStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadingStateViewHolder {
        val binding = LoadingStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadingStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class LoadingStateViewHolder(private val binding: LoadingStateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState) {
            binding.progressBar.isVisible = loadState is LoadState.Loading
        }
    }
}

