package com.example.storyappdicoding.ui.splash

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.storyappdicoding.data.model.UserPreference
import com.example.storyappdicoding.ui.Utils.dataStore
import com.example.storyappdicoding.ui.ViewModelFactory
import com.example.storyappdicoding.ui.login.LoginActivity
import com.example.storyappdicoding.ui.main.MainActivity
import com.example.storydicoding.databinding.ActivitySplashScreenBinding

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var ssViewModel: SplashScreenViewModel
    private var isLogin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        playAnimation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        ssViewModel = ViewModelProvider(
            this, ViewModelFactory(UserPreference.getInstance(dataStore))
        )[SplashScreenViewModel::class.java]

        ssViewModel.apply {
            getUser().observe(this@SplashScreenActivity) {
                this@SplashScreenActivity.isLogin = it.token.isNotEmpty()
            }

            getTheme().observe(this@SplashScreenActivity) {
                if (it)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                else
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun playAnimation() {
        val splash1 = ObjectAnimator.ofFloat(binding.imgSplash, View.ALPHA, 1f).setDuration(2000)
        val splash2 = ObjectAnimator.ofFloat(binding.imgSplash, View.TRANSLATION_Y, 200f).setDuration(800)
        val splash3 = ObjectAnimator.ofFloat(binding.imgSplash, View.TRANSLATION_Y, -10000f).setDuration(600)

        AnimatorSet().apply {
            playSequentially(splash1, splash2, splash3)
            start()
        }.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                val intent = if (isLogin) {
                    Intent(this@SplashScreenActivity, MainActivity::class.java)
                } else {
                    Intent(this@SplashScreenActivity, LoginActivity::class.java)
                }
                startActivity(intent)
                finish()
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })
    }
}