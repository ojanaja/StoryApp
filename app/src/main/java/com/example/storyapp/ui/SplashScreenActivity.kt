package com.example.storyapp.ui

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.example.storyapp.R
import com.example.storyapp.utils.PreferencedManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var preferenceManager: PreferencedManager

    @OptIn(ExperimentalPagingApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        preferenceManager = PreferencedManager(this)

        val textView = findViewById<View>(R.id.bottomTextApp)

        ObjectAnimator.ofFloat(textView, "alpha", 0f, 1f).apply {
            duration = 3500
            interpolator = AccelerateDecelerateInterpolator()
            start()
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {
                    lifecycleScope.launch {
                        delay(2000)

                        val token = preferenceManager.token

                        val intent = if (token.isNotEmpty()) {
                            Intent(this@SplashScreenActivity, HomeScreenActivity::class.java)
                        } else {
                            Intent(this@SplashScreenActivity, LoginScreenActivity::class.java)
                        }
                        startActivity(intent)
                        finish()
                    }
                }

                override fun onAnimationCancel(animation: Animator) {}

                override fun onAnimationRepeat(animation: Animator) {}
            })
        }
    }
}
