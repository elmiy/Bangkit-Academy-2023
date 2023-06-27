package com.example.githubuser.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.data.Result
import com.example.githubuser.data.local.DetailEntity
import com.example.githubuser.databinding.ActivityDetailBinding
import com.example.githubuser.ui.UserViewModel
import com.example.githubuser.ui.ViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var userData: DetailEntity
    private val viewModel by viewModels<UserViewModel>{
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val uname = intent?.getStringExtra(USERNAME)
        val isFav = binding.btnFav

        supportActionBar?.title = "Detail User $uname"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        detailUser(uname.toString(), isFav)

        binding.apply {
            tvName.isSelected = true
            tvName.setSingleLine()
            tvUsername.isSelected = true
            tvUsername.setSingleLine()
            val sectionsPagerAdapter = SectionPagerAdapter(this@DetailActivity)
            sectionsPagerAdapter.username = uname.toString()
            viewPager.adapter = sectionsPagerAdapter
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()

            btnFav.setOnClickListener {
                if (viewModel.isUserFavorite(userData.username)){
                    userData.let {
                        viewModel.setFavorite(it, false)
                    }
                    btnFav.setImageDrawable(ContextCompat.getDrawable(isFav.context, R.drawable.ic_favorite_border))
                } else {
                    userData.let {
                        viewModel.setFavorite(it, true)
                    }
                    btnFav.setImageDrawable(ContextCompat.getDrawable(isFav.context, R.drawable.ic_favorite_full))
                }
            }
        }
    }

    private fun detailUser(uname: String, isFav: FloatingActionButton) {
        viewModel.getDetail(uname).observe(this) {
            if (it != null) {
                binding.apply {
                    when (it) {
                        is Result.Loading-> {
                            progressBar.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            progressBar.visibility = View.GONE
                            userData = it.data

                            tvUsername.text = userData.username
                            if (userData.name != null) { tvName.text = userData.name
                            } else {tvName.text = getString(R.string.replace_name) }
                            Glide.with(this@DetailActivity).load(userData.avatar).into(imgProfpic)
                            tvFollowers.text = resources.getString(R.string.data_followers, userData.followers)
                            tvFollowing.text = resources.getString(R.string.data_following, userData.following)

                            if(userData.isFavorite) {
                                btnFav.setImageDrawable(ContextCompat.getDrawable(isFav.context,R.drawable.ic_favorite_full))
                            } else {
                                btnFav.setImageDrawable(ContextCompat.getDrawable(isFav.context,R.drawable.ic_favorite_border))
                            }
                        }
                        is Result.Error -> {
                            progressBar.visibility = View.GONE
                            Toast.makeText(
                                this@DetailActivity,
                                "Terjadi kesalahan" + it.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
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

    companion object {
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
        const val USERNAME = "username"
    }
}