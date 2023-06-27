package com.example.storyappdicoding.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.storyappdicoding.data.model.UserPreference
import com.example.storyappdicoding.ui.Utils.dataStore
import com.example.storyappdicoding.ui.Utils.isValidEmail
import com.example.storyappdicoding.ui.Utils.isValidPassword
import com.example.storyappdicoding.ui.ViewModelFactory
import com.example.storyappdicoding.ui.login.LoginActivity
import com.example.storydicoding.R
import com.example.storydicoding.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        registerViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[RegisterViewModel::class.java]

        registerViewModel.apply {
            isLoading.observe(this@RegisterActivity) {
                showLoading(it)
            }

            isAnimate.observe(this@RegisterActivity) {
                if (it.hasBeenHandled)
                    showView()
                it.getContentIfNotHandled()?.let {
                    playAnimation()
                }
            }

            userCreated.observe(this@RegisterActivity) {
                if (it == true) {
                    AlertDialog.Builder(this@RegisterActivity).apply {
                        setTitle(getString(R.string.registered))
                        setMessage(getString(R.string.account_created_successfully))
                        setCancelable(false)
                        setPositiveButton(getString(R.string.sign_in)) { _, _ ->
                            finish()
                        }
                        create()
                        show()
                    }
                }
            }
        }
    }

    private fun setupAction() {
        setButtonEnable()
        binding.apply {
            edName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    edName.error = if (edName.text.toString().isEmpty()) {
                        getString(R.string.this_field_cannot_be_blank)
                    } else null
                }

                override fun afterTextChanged(s: Editable?) {
                    setButtonEnable()
                }
            })

            edEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) {
                }

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
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    setButtonEnable()
                }
            })

            edConfirmPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val errorText = if(edConfirmPassword.text.toString() != edPassword.text.toString()) {
                        getString(R.string.password_match)
                    } else null
                    edConfirmPassword.setError(errorText, null)
                }

                override fun afterTextChanged(s: Editable?) {
                    setButtonEnable()
                }
            })

            btnSignup.setOnClickListener {
                binding.let {
                    val name = it.edName.text.toString()
                    val email = it.edEmail.text.toString()
                    val password = it.edPassword.text.toString()
                    registerViewModel.registerUser(name, email, password)
                }
            }

            tvLogin.setOnClickListener {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setButtonEnable() {
        val checkName = binding.edName.text
        val checkEmail = binding.edEmail.text
        val checkPassword = binding.edPassword.text
        val checkConfirmPassword = binding.edConfirmPassword.text
        binding.btnSignup.isEnabled = checkName != null && checkName.toString().isNotEmpty() &&
                checkEmail != null && checkEmail.toString()
            .isNotEmpty() && checkEmail.isValidEmail() &&
                checkPassword != null && checkPassword.toString()
            .isNotEmpty() && checkPassword.toString().isValidPassword() &&
                checkConfirmPassword != null && checkConfirmPassword.toString()
            .isNotEmpty() && checkConfirmPassword.toString() == checkPassword.toString()
    }

    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.tvSignUp, View.ALPHA, 1f).setDuration(500)
        val name = ObjectAnimator.ofFloat(binding.tvName, View.ALPHA, 1f).setDuration(500)
        val nameLayout = ObjectAnimator.ofFloat(binding.edNameLayout, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(500)
        val emailLayout = ObjectAnimator.ofFloat(binding.edEmailLayout, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(500)
        val passwordLayout = ObjectAnimator.ofFloat(binding.edPasswordLayout, View.ALPHA, 1f).setDuration(500)
        val confirm = ObjectAnimator.ofFloat(binding.tvConfirmPassword, View.ALPHA, 1f).setDuration(500)
        val confirmLayout = ObjectAnimator.ofFloat(binding.edConfirmPasswordLayout, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.btnSignup, View.ALPHA,1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.llLogin, View.ALPHA,1f).setDuration(500)


        AnimatorSet().apply {
            playSequentially(title, name, nameLayout, email, emailLayout, password, passwordLayout, confirm, confirmLayout, signup, login)
        }.start()
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
            tvConfirmPassword.alpha = 1f
            edConfirmPasswordLayout.alpha = 1f
            btnSignup.alpha = 1f
            llLogin.alpha = 1f
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}