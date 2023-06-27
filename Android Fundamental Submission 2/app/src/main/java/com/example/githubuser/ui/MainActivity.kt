package com.example.githubuser.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.data.Result
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.databinding.ActivitySettingBinding
import com.example.githubuser.ui.detail.DetailActivity
import com.example.githubuser.ui.favorite.FavoriteActivity
import com.example.githubuser.ui.setting.SettingActivity
import com.example.githubuser.ui.setting.SettingPreference
import com.example.githubuser.ui.setting.SettingViewModel
import com.example.githubuser.ui.setting.SettingViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingTheme: ActivitySettingBinding
    private val viewModel by viewModels<UserViewModel>{
        ViewModelFactory.getInstance(application)
    }

    private val userAdapter = UserAdapter {
        val intentDetail = Intent(this@MainActivity, DetailActivity::class.java)
        intentDetail.putExtra(USERNAME,it.username)
        startActivity(intentDetail)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        bindingTheme = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Github Users"

        val pref = SettingPreference.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this,
            SettingViewModelFactory(pref)
        )[SettingViewModel::class.java]
        val switchTheme = bindingTheme.switchTheme

        settingViewModel.getThemeSettings().observe(this) {
            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        viewModel.getUser().observe(this) {
            if (it != null) {
                when (it) {
                    is Result.Loading-> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val userData = it.data
                        userAdapter.notifyDataSetChanged()
                        userAdapter.submitList(userData)
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            this,
                            "Terjadi kesalahan" + it.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        binding.rvUsers.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = userAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.getSearchUser(query).observe(this@MainActivity) {
                    if (it != null) {
                        when (it) {
                            is Result.Loading-> {
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            is Result.Success -> {
                                binding.progressBar.visibility = View.GONE
                                val userData = it.data
                                userAdapter.submitList(userData)
                            }
                            is Result.Error -> {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(
                                    this@MainActivity,
                                    "Terjadi kesalahan" + it.error,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
                searchView.clearFocus()
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.setting -> {
                val intentSetting = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(intentSetting)
            }
            R.id.favorite -> {
                val intentFavorite = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(intentFavorite)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val USERNAME = "username"
    }
}