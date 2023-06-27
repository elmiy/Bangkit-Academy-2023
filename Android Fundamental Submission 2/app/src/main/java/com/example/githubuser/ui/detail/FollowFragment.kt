package com.example.githubuser.ui.detail

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.data.Result
import com.example.githubuser.databinding.FragmentFollowBinding
import com.example.githubuser.ui.UserAdapter
import com.example.githubuser.ui.UserViewModel
import com.example.githubuser.ui.ViewModelFactory

class FollowFragment : Fragment() {
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<UserViewModel>{
        ViewModelFactory.getInstance(requireActivity())
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

        val adapter = UserAdapter {
            val intentDetail = Intent(requireActivity(), DetailActivity::class.java)
            intentDetail.putExtra(USERNAME, it.username)
            startActivity(intentDetail)
        }

        if (savedInstanceState != null) {
            val state = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                savedInstanceState.getParcelable("recycler_state", DetailActivity::class.java)
            } else {
                savedInstanceState.getParcelable<Parcelable>("recycler_state")
            }
            binding.rvFollows.layoutManager?.onRestoreInstanceState(state as Parcelable?)
        }

        if (position == 1) {
            viewModel.getFollowers(username).observe(viewLifecycleOwner) {
                if (it != null) {
                    when (it) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            val userData = it.data
                            adapter.notifyDataSetChanged()
                            adapter.submitList(userData)
                        }
                        is Result.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                context,
                                "Terjadi kesalahan" + it.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        } else if (position == 2) {
            viewModel.getFollowing(username).observe(viewLifecycleOwner) {
                if (it != null) {
                    when (it) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            val userData = it.data
                            adapter.notifyDataSetChanged()
                            adapter.submitList(userData)
                        }
                        is Result.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                context,
                                "Terjadi kesalahan" + it.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        binding.rvFollows.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            this.adapter = adapter
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("recycler_state", binding.rvFollows.layoutManager?.onSaveInstanceState())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val POSITION = "position"
        const val USERNAME = "username"
    }
}