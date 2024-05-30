package com.example.storyapp.viewModel

import androidx.lifecycle.ViewModel
import com.example.storyapp.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class AddStoryViewModel @Inject constructor(private val dataRepository: DataRepository) : ViewModel() {

    suspend fun uploadStory(auth: String, description: String, file: MultipartBody.Part) =
        dataRepository.uploadStory(auth, description, file)


}