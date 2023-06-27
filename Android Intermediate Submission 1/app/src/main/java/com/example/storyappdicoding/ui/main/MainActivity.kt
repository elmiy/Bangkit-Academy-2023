package com.example.storyappdicoding.ui.main

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyappdicoding.data.model.UserPreference
import com.example.storyappdicoding.data.response.ListStoryItem
import com.example.storyappdicoding.ui.Utils.dataStore
import com.example.storyappdicoding.ui.ViewModelFactory
import com.example.storyappdicoding.ui.addStory.AddStoryActivity
import com.example.storyappdicoding.ui.setting.SettingActivity
import com.example.storydicoding.R
import com.example.storydicoding.databinding.ActivityMainBinding
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupAction()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        AlertDialog.Builder(this).apply {
            setMessage(getString(R.string.are_you_sure))
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                finish()
                exitProcess(0)
            }
            setNegativeButton(getString(R.string.no), null)
        }.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.setting -> {
                val intentSetting = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(intentSetting,
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity).toBundle()
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        mainViewModel.apply {
            isLoading.observe(this@MainActivity) {
                showLoading(it)
            }

            listStory.observe(this@MainActivity) {
                val adapter = MainAdapter(it)
                adapter.setOnItemClickCallback(object : MainAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: ListStoryItem) {
                        val intent = Intent(this@MainActivity, DetailActivity::class.java)
                        intent.putExtra(STORY_EXTRA, data)
                        startActivity(intent,
                            ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity).toBundle()
                        )
                    }
                })
                binding.rvStories.adapter = adapter
            }

            getUser().observe(this@MainActivity) { user ->
                mainViewModel.getStories("Bearer ${user.token}")
            }
        }
    }

    private fun setupAction() {
        val layoutManager = LinearLayoutManager(this)
        binding.apply {
            rvStories.layoutManager = layoutManager
            rvStories.setHasFixedSize(true)

            btnAdd.setOnClickListener {
                val intent = Intent(this@MainActivity, AddStoryActivity::class.java)
                startActivity(intent,
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity).toBundle()
                )
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val STORY_EXTRA = "story"
    }
}