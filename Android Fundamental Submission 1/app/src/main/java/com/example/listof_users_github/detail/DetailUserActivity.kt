package com.example.listof_users_github.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.listof_users_github.R
import com.example.listof_users_github.data.DetailUserResponse
import com.example.listof_users_github.databinding.ActivityDetailUserBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {
    private lateinit var bindingActivityDetailUserActivity: ActivityDetailUserBinding
    private lateinit var detailViewModel: DetailViewModel

    companion object {
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
        const val USERNAME = "username"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingActivityDetailUserActivity = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(bindingActivityDetailUserActivity.root)

        val uname = intent?.getStringExtra(USERNAME)!!

        val actionBar = supportActionBar
        actionBar?.title = "Detail User $uname"
        actionBar?.setDisplayHomeAsUpEnabled(true)

        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)
        detailViewModel.detailUser(uname)

        detailViewModel.userDetail.observe(this) {
            setDetailList(it)
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val sectionsPagerAdapter = SectionPagerAdapter(this)
        sectionsPagerAdapter.username = uname
        val viewPager = bindingActivityDetailUserActivity.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = bindingActivityDetailUserActivity.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun setDetailList(detailList: DetailUserResponse) {
        bindingActivityDetailUserActivity.tvUsername.text = detailList.login
        if (detailList.name != null) { bindingActivityDetailUserActivity.tvName.text = detailList.name
        } else { bindingActivityDetailUserActivity.tvName.text = getString(R.string.replace_name) }
        Glide.with(this).load(detailList.avatarUrl).into(bindingActivityDetailUserActivity.imgProfpic)
        bindingActivityDetailUserActivity.tvFollowers.text = "${detailList.followers} Followers"
        bindingActivityDetailUserActivity.tvFollowing.text = "${detailList.following} Following"
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            bindingActivityDetailUserActivity.progressBar.visibility = View.VISIBLE
        } else {
            bindingActivityDetailUserActivity.progressBar.visibility = View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}