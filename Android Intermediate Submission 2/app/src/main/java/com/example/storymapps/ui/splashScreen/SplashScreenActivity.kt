package com.example.storymapps.ui.splashScreen

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.storymapps.databinding.ActivitySplashScreenBinding
import com.example.storymapps.ui.ViewModelFactory
import com.example.storymapps.ui.login.LoginActivity
import com.example.storymapps.ui.main.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var viewModel: SplashScreenViewModel
    private var isLogin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        playAnimation()
        setupViewModel()
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
        viewModel = ViewModelProvider(
            this, ViewModelFactory.getInstance(this)
        )[SplashScreenViewModel::class.java]

        viewModel.apply {
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
        val stay = ObjectAnimator.ofFloat(binding.imgLogo, View.ALPHA, 1f).setDuration(2000)
        val exit = ObjectAnimator.ofFloat(binding.imgLogo, View.TRANSLATION_X, 10000f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(stay, exit)
            start()
        }.addListener(object : Animator.AnimatorListener{
            override fun onAnimationStart(animation: Animator) {
                //Do Nothing
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
                //Do Nothing
            }

            override fun onAnimationRepeat(animation: Animator) {
                //Do Nothing
            }

        })
    }
}