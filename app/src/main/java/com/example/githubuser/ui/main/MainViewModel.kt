package com.example.githubuser.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.githubuser.data.local.datastore.SettingPreferences
import com.example.githubuser.data.remote.retrofit.ApiConfig
import com.example.githubuser.utils.Event
import com.example.githubuser.data.remote.response.ItemsItem
import com.example.githubuser.data.remote.response.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: SettingPreferences): ViewModel() {

    private val _users = MutableLiveData<List<ItemsItem>>()
    val users: LiveData<List<ItemsItem>> = _users

    private val _isLoading = MutableLiveData<Boolean>()
    val isloading: LiveData<Boolean> = _isLoading

    private val _snackbar = MutableLiveData<Event<String>>()
    val snackbar: LiveData<Event<String>> = _snackbar

    companion object{
        private const val TAG = "MainViewModel"
        private const val DEFAULT_USERNAME = "ahmad"
    }

    init {
        searchGitUser(DEFAULT_USERNAME)
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }
    fun searchGitUser(query: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getListUsers(query)
        client.enqueue(object: Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                _isLoading.value = false

                val userResponse = response.body()
                if (response.isSuccessful && userResponse != null){
                    _users.value = userResponse.items
                    if(userResponse.totalCount == 0){
                        _snackbar.value = Event("User Not Found")
                    }
                }
                else {
                    Log.e(TAG, "onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }


}