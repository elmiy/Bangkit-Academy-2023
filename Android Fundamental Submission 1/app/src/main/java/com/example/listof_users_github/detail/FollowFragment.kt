package com.example.listof_users_github.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listof_users_github.data.DataUser
import com.example.listof_users_github.data.ItemsItem
import com.example.listof_users_github.databinding.FragmentFollowBinding
import com.example.listof_users_github.main.UsersAdapter

class FollowFragment: Fragment() {
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var adapter: UsersAdapter

    companion object {
        const val POSITION = "position"
        const val USERNAME = "username"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = arguments?.getString(USERNAME).toString()
        val position = arguments?.getInt(POSITION, 0)
        _binding = FragmentFollowBinding.bind(view)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollows.layoutManager = layoutManager
        binding.rvFollows.setHasFixedSize(true)
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvFollows.addItemDecoration(itemDecoration)

        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)

        if (position == 1) {
            detailViewModel.userFollowers.observe(viewLifecycleOwner) {
                setFollowersList(it)
            }
            detailViewModel.followersUser(username)
        } else {
            detailViewModel.userFollowing.observe(viewLifecycleOwner) {
                setFollowingList(it)
            }
            detailViewModel.followingUser(username)
        }

        detailViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun setFollowersList(followers: List<ItemsItem>) {
        val listReview = ArrayList<DataUser>()
        for (user in followers) {
            val data = DataUser(user.login, user.avatarUrl)
            listReview.add(data)
        }
        adapter = UsersAdapter(listReview)
        binding.rvFollows.adapter = adapter
        adapter.setOnItemClickCallback(object : UsersAdapter.OnItemClickCallback{
            override fun onItemClicked(data: DataUser) {
                showSelectedUsers(data)
            }
        })
    }

    private fun setFollowingList(following: List<ItemsItem>) {
        val listReview = ArrayList<DataUser>()
        for (user in following) {
            val data = DataUser(user.login, user.avatarUrl)
            listReview.add(data)
        }
        adapter = UsersAdapter(listReview)
        binding.rvFollows.adapter = adapter
        adapter.setOnItemClickCallback(object : UsersAdapter.OnItemClickCallback{
            override fun onItemClicked(data: DataUser) {
                showSelectedUsers(data)
            }
        })
    }

    private fun showSelectedUsers(user: DataUser) {
        val intentDetail = Intent(activity, DetailUserActivity::class.java)
        intentDetail.putExtra(USERNAME, user.login)
        activity?.startActivity(intentDetail)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}