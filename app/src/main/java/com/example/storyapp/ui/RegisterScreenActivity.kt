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
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityRegisterScreenBinding
import com.example.storyapp.utils.NetworkRequest
import com.example.storyapp.viewModel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterScreenActivity : AppCompatActivity() {

    private val binding: ActivityRegisterScreenBinding by lazy {
        ActivityRegisterScreenBinding.inflate(layoutInflater)
    }
    private val viewModel: RegisterViewModel by viewModels()
    private var registerJob: Job = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupRegisterButton()
        setupSignInTextView()
        playPropertyAnimation()
    }

    private fun setupRegisterButton() {
        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString().trim()
            val email = binding.edRegisterEmail.text.toString().trim()
            val password = binding.edRegisterPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showToast(R.string.warning_input)
            } else {
                registerUser(name, email, password)
            }
        }
    }

    private fun setupSignInTextView() {
        binding.tvSignIn.setOnClickListener {
            startActivity(Intent(this, LoginScreenActivity::class.java))
            finish()
        }
    }

    private fun playPropertyAnimation() {
        val title = ObjectAnimator.ofFloat(binding.textView, View.ALPHA, 0f, 1f).setDuration(500)
        val edName = ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 0f, 1f).setDuration(500).apply { startDelay = 100 }
        val edEmail = ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 0f, 1f).setDuration(500).apply { startDelay = 200 }
        val edPassword = ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 0f, 1f).setDuration(500).apply { startDelay = 300 }
        val btnRegister = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 0f, 1f).setDuration(500).apply { startDelay = 400 }

        AnimatorSet().apply {
            playTogether(title, edName, edEmail, edPassword, btnRegister)
            start()
        }
    }

    private fun registerUser(name: String, email: String, password: String) {
        setLoadingState(true)
        lifecycle.coroutineScope.launchWhenResumed {
            if (registerJob.isActive) registerJob.cancel()
            registerJob = launch {
                viewModel.register(name, email, password).collect { result ->
                    when (result) {
                        is NetworkRequest.Success -> handleRegisterSuccess()
                        is NetworkRequest.Loading -> setLoadingState(true)
                        is NetworkRequest.Error -> handleRegisterError()
                    }
                }
            }
        }
    }

    private fun handleRegisterSuccess() {
        setLoadingState(false)
        showToast(R.string.success_register)
        startActivity(Intent(this, LoginScreenActivity::class.java))
        finish()
    }

    private fun handleRegisterError() {
        setLoadingState(false)
        showToast(R.string.error_register)
    }

    private fun setLoadingState(loading: Boolean) {
        binding.btnRegister.visibility = if (loading) View.INVISIBLE else View.VISIBLE
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

    private fun showToast(messageResId: Int) {
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}
