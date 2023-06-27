package com.example.storymapps.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storymapps.R
import com.example.storymapps.data.model.UserModel
import com.example.storymapps.data.online.response.ListStoryItem
import com.example.storymapps.databinding.ActivityMainBinding
import com.example.storymapps.ui.ViewModelFactory
import com.example.storymapps.ui.addStory.AddStoryActivity
import com.example.storymapps.ui.login.LoginActivity
import com.example.storymapps.ui.main.DetailActivity.Companion.STORY_EXTRA
import com.example.storymapps.ui.main.adapter.LoadingStateAdapter
import com.example.storymapps.ui.main.adapter.MainAdapter
import com.example.storymapps.ui.maps.MapsActivity
import com.example.storymapps.ui.setting.SettingActivity
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private val adapter = MainAdapter()
    private var userModel = UserModel("","","")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.stories_page)

        setupViewModel()
        setupAction()
        setupDetail()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_maps -> {
                val intent = Intent(this@MainActivity, MapsActivity::class.java)
                    .putExtra(TOKEN_MAIN, userModel)
                startActivity(intent,
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity).toBundle())
            }
            R.id.menu_setting -> {
                val intent = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(intent,
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity).toBundle())
            }
            R.id.menu_logout -> {
                logout()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        AlertDialog.Builder(this, R.style.MyAlertDialog).apply {
            setTitle(getString(R.string.exit))
            setMessage(getString(R.string.confirm_exit))
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                finishAffinity()
                exitProcess(0)
            }
            setNegativeButton(getString(R.string.no), null)
            show()
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this, ViewModelFactory.getInstance(this)
        )[MainViewModel::class.java]

        viewModel.getUser().observe(this@MainActivity) { result ->
            val tokenPass = result.token
            userModel = result
            viewModel.getStories(tokenPass).observe(this) {
                adapter.submitData(lifecycle, it)
            }
        }
    }

    private fun setupAction() {
        binding.apply {
            rvStories.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                }
            )
            rvStories.layoutManager = LinearLayoutManager(this@MainActivity)
            rvStories.setHasFixedSize(true)

            btnAdd.setOnClickListener {
                val intent = Intent(this@MainActivity, AddStoryActivity::class.java)
                    .putExtra(TOKEN_MAIN, userModel)
                startActivity(intent,
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity).toBundle())
            }
        }
    }

    private fun setupDetail() {
        adapter.setOnItemClickCallback(object : MainAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListStoryItem) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(STORY_EXTRA, data)
                startActivity(intent)
            }
        })
    }

    private fun logout() {
        AlertDialog.Builder(this, R.style.MyAlertDialog).apply {
            setTitle(getString(R.string.logout))
            setMessage(getString(R.string.confirm_logout))
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.logout()
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            setNegativeButton(getString(R.string.no), null)
            show()
        }
    }

    companion object {
        const val TOKEN_MAIN = "token"
    }
}