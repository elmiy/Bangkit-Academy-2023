package com.example.githubuser.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.databinding.ActivityFavoriteBinding
import com.example.githubuser.ui.MainActivity
import com.example.githubuser.ui.UserAdapter
import com.example.githubuser.ui.UserViewModel
import com.example.githubuser.ui.ViewModelFactory
import com.example.githubuser.ui.detail.DetailActivity

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private val viewModel by viewModels<UserViewModel> {
        ViewModelFactory.getInstance(application)
    }

    private val userAdapter = UserAdapter {
        val intentDetail = Intent(this@FavoriteActivity, DetailActivity::class.java)
        intentDetail.putExtra(MainActivity.USERNAME,it.username)
        startActivity(intentDetail)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favorite Users"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.getFavorite().observe(this) {
            userAdapter.notifyDataSetChanged()
            userAdapter.submitList(it)
        }

        binding.rvUsers.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = userAdapter
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