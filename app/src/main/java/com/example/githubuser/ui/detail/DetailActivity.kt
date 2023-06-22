package com.example.githubuser.ui.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuser.data.remote.response.DetailUserResponse
import com.example.githubuser.R
import com.example.githubuser.data.local.entity.FavoriteUser
import com.example.githubuser.utils.SectionPagerAdapter
import com.example.githubuser.databinding.ActivityDetailBinding
import com.example.githubuser.ui.setting.SettingActivity
import com.example.githubuser.utils.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class DetailActivity : AppCompatActivity() {

    private lateinit var activityDetailBinding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel>() {
        ViewModelFactory.getInstance(application)
    }
    private lateinit var username: String
    private lateinit var avatarUrl: String
    private var isFavorite: Boolean = false
    private var detailFavorite: MenuItem? = null

    companion object {
        const val USERNAME = "username"
        const val AVATAR = "avatarUrl"
        const val TAG = "DetailActivity"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDetailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(activityDetailBinding.root)

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.userProfile.observe(this) {
            setUserProfile(it)
        }

        val sectionsPagerAdapter = SectionPagerAdapter(this)
        val viewPager: ViewPager2 = activityDetailBinding.viewPager
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = activityDetailBinding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, i ->
            tab.text = resources.getString(TAB_TITLES[i])
        }.attach()

        username = intent.getStringExtra(USERNAME).toString()
        avatarUrl = intent.getStringExtra(AVATAR).toString()
        viewModel.findUserProfile(username)
        sectionsPagerAdapter.username = username

        supportActionBar?.title = username
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.detail_favorite -> {
                if (!isFavorite) {
                    detailFavorite?.setIcon(R.drawable.baseline_favorite_24)
                    val user = FavoriteUser()
                    user.username = username
                    user.avatarUrl = avatarUrl
                    viewModel.setFavoriteUser(user)
                    isFavorite = true
                } else {
                    detailFavorite?.setIcon(R.drawable.baseline_favorite_border_24)
                    viewModel.delete(username)
                    isFavorite = false
                }
            }
            R.id.detail_setting->{
                Intent(this, SettingActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        detailFavorite = menu?.findItem(R.id.detail_favorite)
        viewModel.getFavoriteUserByUsername(username).observe(this) { favUser ->
            isFavorite = if (favUser == null) {
                detailFavorite?.setIcon(R.drawable.baseline_favorite_border_24)
                false
            } else {
                detailFavorite?.setIcon(R.drawable.baseline_favorite_24)
                true
            }
        }
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(
            "isLoadingVisible",
            activityDetailBinding.progressBarDetail.visibility == View.VISIBLE
        )
    }

    private fun setUserProfile(userObject: DetailUserResponse) {
        activityDetailBinding.tvUsernameDetail.text = userObject.login
        activityDetailBinding.tvNameDetail.text = userObject.name
        activityDetailBinding.tvFollowersCount.text = userObject.followers.toString()
        activityDetailBinding.tvFollowingCount.text = userObject.following.toString()
        Glide.with(activityDetailBinding.root)
            .load(userObject.avatarUrl)
            .into(activityDetailBinding.ivDetailUser)
    }

    private fun showLoading(isLoading: Boolean) {
        activityDetailBinding.progressBarDetail.visibility =
            if (isLoading) View.VISIBLE else View.GONE
    }
}