package com.example.storymapps.ui.login

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.storymapps.R
import com.example.storymapps.data.Result
import com.example.storymapps.data.model.UserModel
import com.example.storymapps.databinding.ActivityLoginBinding
import com.example.storymapps.ui.Utils.isValidEmail
import com.example.storymapps.ui.Utils.isValidPassword
import com.example.storymapps.ui.ViewModelFactory
import com.example.storymapps.ui.main.MainActivity
import com.example.storymapps.ui.register.RegisterActivity


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        playAnimation()
        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this, ViewModelFactory.getInstance(this)
        )[LoginViewModel::class.java]
    }

    private fun setupAction() {
        enableButton()
        binding.apply {
            edEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                    // Do nothing.
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (edEmail.text.toString().isEmpty()){
                        edEmail.error = getString(R.string.no_empty)
                    } else if (!s.isValidEmail()) {
                        edEmail.error = getString(R.string.wrong_format)
                    }
                }

                override fun afterTextChanged(s: Editable) {
                    enableButton()
                }
            })

            edPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                    // Do nothing.
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    // Already determined on CustomView
                }

                override fun afterTextChanged(s: Editable) {
                    enableButton()
                }
            })

            btnLogin.setOnClickListener {
                val email = binding.edEmail.text.toString()
                val password = binding.edPassword.text.toString()
                viewModel.login(email, password).observe(this@LoginActivity) { result ->
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Success -> {
                            val name = result.data.loginResult.name
                            val token = result.data.loginResult.token
                            viewModel.saveUser(UserModel(name, email, token))
                            successLogin()
                        }
                        is Result.Error -> {
                            showLoading(false)
                            if (result.error.contains("401")) {
                                invalidAccount()
                            } else {
                                invalidLogin(result.error)
                            }
                            Log.d(TAG, result.error)
                        }
                    }
                }
            }

            tvRegister.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun enableButton() {
        val email = binding.edEmail.text
        val password = binding.edPassword.text.toString()
        (email != null && email.toString().isNotEmpty() && email.isValidEmail()
                && password.isNotEmpty() && password.isValidPassword()).also {
            binding.btnLogin.isEnabled = it }
    }

    private fun successLogin() {
        AlertDialog.Builder(this, R.style.MyAlertDialog).apply {
            setTitle(getString(R.string.congrats))
            setMessage(getString(R.string.login_success))
            setPositiveButton(getString(R.string.next)) { _, _ ->
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent,
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoginActivity).toBundle())
                finish()
            }
            create()
            show()
        }
    }

    private fun invalidAccount() {
        AlertDialog.Builder(this, R.style.MyAlertDialog).apply {
            setTitle(getString(R.string.invalid_login))
            setMessage(getString(R.string.invalid_account))
            setPositiveButton(getString(R.string.ok)) { ok, _ ->
                ok.cancel()
            }
            create()
            show()
        }
    }

    private fun invalidLogin(message: String?) {
        AlertDialog.Builder(this, R.style.MyAlertDialog).apply {
            setTitle(getString(R.string.invalid_login))
            setMessage(message)
            setPositiveButton(getString(R.string.ok)) { ok, _ ->
                ok.cancel()
            }
            create()
            show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(300)
        val email = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(300)
        val emailLayout = ObjectAnimator.ofFloat(binding.edEmailLayout, View.ALPHA, 1f).setDuration(300)
        val password = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(300)
        val passwordLayout = ObjectAnimator.ofFloat(binding.edPasswordLayout, View.ALPHA, 1f).setDuration(300)
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA,1f).setDuration(300)
        val register = ObjectAnimator.ofFloat(binding.llRegister, View.ALPHA,1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(title, email, emailLayout, password, passwordLayout, login, register)
            start()
        }.addListener(object : Animator.AnimatorListener{
            override fun onAnimationStart(animation: Animator) {
                //Do Nothing
            }

            override fun onAnimationEnd(animation: Animator) {
                showView()
            }

            override fun onAnimationCancel(animation: Animator) {
                //Do Nothing
            }

            override fun onAnimationRepeat(animation: Animator) {
                //Do Nothing
            }
        })
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

    companion object {
        const val TAG = "LoginActivity"
    }
}