package com.example.storyapp.viewModel

import androidx.lifecycle.ViewModel
import com.example.storyapp.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val dataRepository: DataRepository) : ViewModel() {
    suspend fun login(email: String, password: String) = dataRepository.login(email, password)

}
