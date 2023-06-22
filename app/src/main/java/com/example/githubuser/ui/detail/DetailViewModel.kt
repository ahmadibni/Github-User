package com.example.githubuser.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.local.UserRepository
import com.example.githubuser.data.local.entity.FavoriteUser
import com.example.githubuser.data.remote.retrofit.ApiConfig
import com.example.githubuser.data.remote.response.DetailUserResponse
import com.example.githubuser.data.remote.response.ItemsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application): ViewModel() {
    private val userRepository: UserRepository = UserRepository(application)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _userProfile = MutableLiveData<DetailUserResponse>()
    val userProfile: LiveData<DetailUserResponse> = _userProfile

    private val _followers = MutableLiveData<List<ItemsItem>>()
    val followers: LiveData<List<ItemsItem>> = _followers

    private val _following = MutableLiveData<List<ItemsItem>>()
    val following: LiveData<List<ItemsItem>> = _following

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> = userRepository.getFavoriteUserByUsername(username)

    fun setFavoriteUser(user: FavoriteUser){
        userRepository.setFavoriteUser(user)
    }

    fun delete(username: String){
        userRepository.delete(username)
    }

    var user: String? = null

    fun findUserProfile(username: String = ""){
        if (user == null){
            user = username


            _isLoading.value = true
            val client = ApiConfig.getApiService().getDetailUser(username)
            client.enqueue(object: Callback<DetailUserResponse> {
                override fun onResponse(call: Call<DetailUserResponse>, response: Response<DetailUserResponse>) {
                    _isLoading.value = false
                    if (response.isSuccessful){
                        _userProfile.value = response.body()
                    }
                    else {
                        Log.e(DetailActivity.TAG, "onResponse: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(DetailActivity.TAG, "onFailure: ${t.message}")
                }
            })
        }
    }

    fun getFollowers(query: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(query)
        client.enqueue(object: Callback<List<ItemsItem>> {
            override fun onResponse(call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>) {
                _isLoading.value = false
                val followResponse = response.body()
                if (response.isSuccessful && followResponse != null){
                    _followers.value = response.body()
                }
                else {
                    Log.e(DetailActivity.TAG, "onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(DetailActivity.TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getFollowing(query: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(query)
        client.enqueue(object: Callback<List<ItemsItem>> {
            override fun onResponse(call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>) {
                _isLoading.value = false
                val followResponse = response.body()
                if (response.isSuccessful && followResponse != null){
                    _following.value = response.body()
                }
                else {
                    Log.e(DetailActivity.TAG, "onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(DetailActivity.TAG, "onFailure: ${t.message}")
            }
        })
    }


}