package com.example.listof_users_github.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listof_users_github.R
import com.example.listof_users_github.data.DataUser
import com.example.listof_users_github.data.ItemsItem
import com.example.listof_users_github.databinding.ActivityMainBinding
import com.example.listof_users_github.detail.DetailUserActivity

class MainActivity : AppCompatActivity() {
    private lateinit var bindingActivityMainBinding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingActivityMainBinding.root)

        val actionBar = supportActionBar
        actionBar?.title = "Github Users"

        val layoutManager = LinearLayoutManager(this)
        bindingActivityMainBinding.rvUsers.layoutManager = layoutManager
        bindingActivityMainBinding.rvUsers.setHasFixedSize(true)
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        bindingActivityMainBinding.rvUsers.addItemDecoration(itemDecoration)

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
        mainViewModel.listUser.observe(this) {
            setUserList(it)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setUserList(usersList: List<ItemsItem>) {
        val userData = ArrayList<DataUser>()
        for (user in usersList) {
            val list = DataUser(user.login, user.avatarUrl)
            userData.add(list)
        }
        val adapter = UsersAdapter(userData)
        bindingActivityMainBinding.rvUsers.adapter = adapter
        adapter.setOnItemClickCallback(object : UsersAdapter.OnItemClickCallback{
            override fun onItemClicked(data: DataUser) {
                showSelectedUsers(data)
            }
        })
    }

    private fun showSelectedUsers(user: DataUser) {
        val intentDetail = Intent(this@MainActivity, DetailUserActivity::class.java)
        intentDetail.putExtra(DetailUserActivity.USERNAME, user.login)
        startActivity(intentDetail)
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
                mainViewModel.searchUser(query)
                searchView.clearFocus()
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            bindingActivityMainBinding.progressBar.visibility = View.VISIBLE
        } else {
            bindingActivityMainBinding.progressBar.visibility = View.GONE
        }
    }
}