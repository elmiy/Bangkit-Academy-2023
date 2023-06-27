package com.example.storyappdicoding.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.storyappdicoding.data.model.UserPreference
import com.example.storyappdicoding.ui.Utils.dataStore
import com.example.storyappdicoding.ui.Utils.isValidEmail
import com.example.storyappdicoding.ui.Utils.isValidPassword
import com.example.storyappdicoding.ui.ViewModelFactory
import com.example.storyappdicoding.ui.main.MainActivity
import com.example.storyappdicoding.ui.register.RegisterActivity
import com.example.storydicoding.R
import com.example.storydicoding.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]

        loginViewModel.apply {
            isLoading.observe(this@LoginActivity) {
                showLoading(it)
            }

            isAnimate.observe(this@LoginActivity) {
                if (it.hasBeenHandled)
                    showView()
                it.getContentIfNotHandled()?.let {
                    playAnimation()
                }
            }

            isLoginError.observe(this@LoginActivity) {
                showLoginInvalid(it)
                if (!it) {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun setupAction() {
        setButtonEnable()
        binding.apply {
            edEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    edEmail.error = if (edEmail.text.toString().isEmpty()) {
                        getString(R.string.this_field_cannot_be_blank)
                    } else if (!s.isValidEmail()) {
                        getString(R.string.email_is_invalid)
                    } else null
                }

                override fun afterTextChanged(s: Editable?) {
                    setButtonEnable()
                }
            })

            edPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    setButtonEnable()
                    showLoginInvalid(false)
                }
            })

            btnLogin.setOnClickListener {
                binding.let {
                    val email = it.edEmail.text.toString()
                    val password = it.edPassword.text.toString()
                    loginViewModel.login(email, password)
                }
            }

            tvRegister.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setButtonEnable() {
        val checkEmail = binding.edEmail.text
        val checkPassword = binding.edPassword.text

        binding.btnLogin.isEnabled =
            checkEmail != null && checkEmail.toString().isNotEmpty() && checkEmail.isValidEmail() &&
                    checkPassword != null && checkPassword.toString().isNotEmpty() &&
                    checkPassword.toString().isValidPassword()
    }

    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(500)
        val emailLayout = ObjectAnimator.ofFloat(binding.edEmailLayout, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(500)
        val passwordLayout = ObjectAnimator.ofFloat(binding.edPasswordLayout, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA,1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.llRegister, View.ALPHA,1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title, email, emailLayout, password, passwordLayout, login, signup)
        }.start()
    }

    private fun showView() {
        binding.apply {
            tvLogin.alpha = 1f
            tvEmail.alpha = 1f
            edEmailLayout.alpha = 1f
            tvPassword.alpha = 1f
            edPasswordLayout.alpha = 1f
            btnLogin.alpha = 1f
            llRegister.alpha = 1f
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showLoginInvalid(isError: Boolean) {
        if (isError) {
            binding.cvLoginInvalid.visibility = View.VISIBLE
        } else {
            binding.cvLoginInvalid.visibility = View.GONE
        }
    }
}