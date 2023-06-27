package com.example.storyappdicoding.ui.setting

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.storyappdicoding.data.model.UserPreference
import com.example.storyappdicoding.ui.Utils.dataStore
import com.example.storyappdicoding.ui.ViewModelFactory
import com.example.storyappdicoding.ui.login.LoginActivity
import com.example.storydicoding.R
import com.example.storydicoding.databinding.ActivitySettingBinding
import java.util.Locale

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    private lateinit var settingViewModel: SettingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        settingViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[SettingViewModel::class.java]

        settingViewModel.getThemeSetting().observe(this) {
            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false
            }
        }
    }

    private fun setupAction() {
        binding.apply {
            btnLogout.setOnClickListener {
                AlertDialog.Builder(this@SettingActivity).apply {
                    setTitle(getString(R.string.logout))
                    setMessage(getString(R.string.are_you_sure_logout))
                    setPositiveButton(getString(R.string.yes)) { _, _ ->
                        settingViewModel.logout()
                        val intent = Intent(this@SettingActivity, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                    setNegativeButton(getString(R.string.no), null)
                }.show()
            }

            switchTheme.setOnCheckedChangeListener { _, isChecked ->
                settingViewModel.saveThemeSetting(isChecked)
            }

            btnLanguage.text = Locale.getDefault().language
            btnLanguage.setOnClickListener {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}