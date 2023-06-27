package com.example.storymapps.ui.register

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.storymapps.R
import com.example.storymapps.data.Result
import com.example.storymapps.databinding.ActivityRegisterBinding
import com.example.storymapps.ui.Utils.isValidEmail
import com.example.storymapps.ui.Utils.isValidPassword
import com.example.storymapps.ui.ViewModelFactory
import com.example.storymapps.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        playAnimation()
        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this, ViewModelFactory.getInstance(this)
        )[RegisterViewModel::class.java]
    }

    private fun setupAction() {
        enableButton()
        binding.apply {
            edName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                    // Do nothing.
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (edName.text.toString().isEmpty()){
                        edName.error = getString(R.string.no_empty)
                    }
                }

                override fun afterTextChanged(s: Editable) {
                    enableButton()
                }
            })

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

            btnRegister.setOnClickListener {
                val name = binding.edName.text.toString()
                val email = binding.edEmail.text.toString()
                val password = binding.edPassword.text.toString()
                viewModel.register(name, email, password).observe(this@RegisterActivity) { result ->
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Success -> {
                            showLoading(false)
                            successRegister()
                        }
                        is Result.Error -> {
                            showLoading(false)
                            if (result.error.contains("400")) {
                                invalidEmail()
                            } else {
                                invalidRegister(result.error)
                            }
                            Log.d(TAG, result.error)
                        }
                    }
                }
            }

            tvLogin.setOnClickListener {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun enableButton() {
        val name = binding.edName.text.toString()
        val email = binding.edEmail.text
        val password = binding.edPassword.text.toString()
        (name.isNotEmpty() && email != null && email.toString().isNotEmpty() && email.isValidEmail()
                && password.isNotEmpty() && password.isValidPassword()).also {
            binding.btnRegister.isEnabled = it }
    }

    private fun successRegister() {
        AlertDialog.Builder(this, R.style.MyAlertDialog).apply {
            setTitle(getString(R.string.congrats))
            setMessage(getString(R.string.register_success))
            setPositiveButton(getString(R.string.login)) { _, _ ->
                finish()
            }
            create()
            show()
        }
    }

    private fun invalidEmail() {
        AlertDialog.Builder(this, R.style.MyAlertDialog).apply {
            setTitle(getString(R.string.invalid_register))
            setMessage(getString(R.string.invalid_email))
            setPositiveButton(getString(R.string.ok)) { ok, _ ->
                ok.cancel()
            }
            create()
            show()
        }
    }

    private fun invalidRegister(message: String?) {
        AlertDialog.Builder(this, R.style.MyAlertDialog).apply {
            setTitle(getString(R.string.invalid_register))
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
        val title = ObjectAnimator.ofFloat(binding.tvRegister, View.ALPHA, 1f).setDuration(300)
        val name = ObjectAnimator.ofFloat(binding.tvName, View.ALPHA, 1f).setDuration(300)
        val nameLayout = ObjectAnimator.ofFloat(binding.edNameLayout, View.ALPHA, 1f).setDuration(300)
        val email = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(300)
        val emailLayout = ObjectAnimator.ofFloat(binding.edEmailLayout, View.ALPHA, 1f).setDuration(300)
        val password = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(300)
        val passwordLayout = ObjectAnimator.ofFloat(binding.edPasswordLayout, View.ALPHA, 1f).setDuration(300)
        val register = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA,1f).setDuration(300)
        val login = ObjectAnimator.ofFloat(binding.llLogin, View.ALPHA,1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(title, name, nameLayout, email, emailLayout, password, passwordLayout, register, login)
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
            tvName.alpha = 1f
            edNameLayout.alpha = 1f
            tvEmail.alpha = 1f
            edEmailLayout.alpha = 1f
            tvPassword.alpha = 1f
            edPasswordLayout.alpha = 1f
            btnRegister.alpha = 1f
            llLogin.alpha = 1f
        }
    }

    companion object {
        const val TAG = "RegisterActivity"
    }
}