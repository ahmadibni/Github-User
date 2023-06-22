package com.example.githubuser.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.data.remote.response.ItemsItem
import com.example.githubuser.databinding.FragmentDetailListBinding
import com.example.githubuser.ui.main.MainAdapter
import com.example.githubuser.utils.ViewModelFactory

class DetailListFragment : Fragment() {
    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }

    private lateinit var binding: FragmentDetailListBinding
    private val viewModel by viewModels<DetailViewModel>() {
        ViewModelFactory.getInstance(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var position = 0
        var username = ""

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME).toString()
        }

        viewModel.followers.observe(viewLifecycleOwner) {
            if (it != null){
                setFollow(it)
            }
        }
        viewModel.following.observe(viewLifecycleOwner) {
            if (it != null){
                setFollow(it)
            }
        }
        viewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }

        if (position == 1) {
            viewModel.getFollowers(username)
        } else {
            viewModel.getFollowing(username)
        }
    }

    private fun setFollow(listFollow: List<ItemsItem>) {
        binding.rvDetail.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvDetail.adapter = MainAdapter(listFollow)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarFollow.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}