package com.example.storyapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.repository.StoryRepository
import com.example.storyapp.data.Story
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val storyRepository: StoryRepository): ViewModel() {

    fun getStories(auth: String) : LiveData<PagingData<Story>> =
        storyRepository.getStories(auth).cachedIn(viewModelScope).asLiveData()
}