package com.example.githubuser.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.data.remote.response.ItemsItem
import com.example.githubuser.databinding.ActivityFavoriteBinding
import com.example.githubuser.ui.main.MainAdapter
import com.example.githubuser.ui.setting.SettingActivity
import com.example.githubuser.utils.ViewModelFactory

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private val viewModel by viewModels<FavoriteViewModel>() {
        ViewModelFactory.getInstance(application)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favorite User"
        showFavoriteUser()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favorite_setting->{
                Intent(this, SettingActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorite_menu, menu)
        return true
    }

    private fun showFavoriteUser(){
        viewModel.getFavoriteUser().observe(this) { user ->
            val items: MutableList<ItemsItem> = mutableListOf()
            user.map {
                val item = ItemsItem(login = it.username.toString(), avatarUrl = it.avatarUrl.toString())
                items.add(item)
            }
            binding.rvFavorite.apply {
                layoutManager = LinearLayoutManager(this@FavoriteActivity)
                adapter = MainAdapter(items)
                setHasFixedSize(true)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        showFavoriteUser()
    }
}