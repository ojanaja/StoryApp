package com.example.storyapp.viewModel

import androidx.lifecycle.ViewModel
import com.example.storyapp.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val dataRepository: DataRepository) : ViewModel()  {
    suspend fun register(name: String, email: String, password: String) = dataRepository.register(name, email, password)
}