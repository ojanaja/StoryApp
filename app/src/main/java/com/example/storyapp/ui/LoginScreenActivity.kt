package com.example.storyapp.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import androidx.paging.ExperimentalPagingApi
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityLoginScreenBinding
import com.example.storyapp.utils.NetworkRequest
import com.example.storyapp.utils.PreferencedManager
import com.example.storyapp.viewModel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginScreenActivity : AppCompatActivity() {

    private val binding: ActivityLoginScreenBinding by lazy {
        ActivityLoginScreenBinding.inflate(layoutInflater)
    }
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var preferenceManager: PreferencedManager
    private var loginJob: Job = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()
        preferenceManager = PreferencedManager(this)
        preferenceManager.isExampleLogin = true
        setupLoginButton()
        setupRegisterTextView()
        playPropertyAnimation()
    }

    private fun setupLoginButton() {
        binding.btnSignIn.setOnClickListener {
            val email = binding.edLoginEmail.text.toString().trim()
            val password = binding.edLoginPassword.text.toString().trim()
            if (email.isEmpty() || password.isEmpty()) {
                showToast(R.string.warning_input)
            } else {
                loginUser(email, password)
            }
        }
    }

    private fun setupRegisterTextView() {
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterScreenActivity::class.java))
        }
    }

    private fun playPropertyAnimation() {
        val title = ObjectAnimator.ofFloat(binding.textView, View.ALPHA, 0f, 1f).setDuration(500)
        val edEmail = ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 0f, 1f).setDuration(500).apply { startDelay = 200 }
        val edPassword = ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 0f, 1f).setDuration(500).apply { startDelay = 300 }
        val btnSignIn = ObjectAnimator.ofFloat(binding.btnSignIn, View.ALPHA, 0f, 1f).setDuration(500).apply { startDelay = 400 }

        AnimatorSet().apply {
            playTogether(title, edEmail, edPassword, btnSignIn)
            start()
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    private fun loginUser(email: String, password: String) {
        setLoadingState(true)
        lifecycle.coroutineScope.launchWhenResumed {
            if (loginJob.isActive) loginJob.cancel()
            loginJob = launch {
                viewModel.login(email, password).collect { result ->
                    when (result) {
                        is NetworkRequest.Success -> handleLoginSuccess(result.data?.result?.token)
                        is NetworkRequest.Loading -> setLoadingState(true)
                        is NetworkRequest.Error -> handleLoginError()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    private fun handleLoginSuccess(token: String?) {
        setLoadingState(false)
        token?.let {
            preferenceManager.exampleBoolean = true
            preferenceManager.token = it
            startActivity(Intent(this, HomeScreenActivity::class.java))
            finish()
        }
    }

    private fun handleLoginError() {
        setLoadingState(false)
        showToast(R.string.check)
    }

    private fun setLoadingState(loading: Boolean) {
        binding.btnSignIn.visibility = if (loading) View.INVISIBLE else View.VISIBLE
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

    private fun showToast(messageResId: Int) {
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }
}
