package com.example.githubuser.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.local.UserRepository
import com.example.githubuser.data.local.entity.FavoriteUser

class FavoriteViewModel(application: Application): ViewModel() {
    private val userRepository: UserRepository = UserRepository(application)

    fun getFavoriteUser(): LiveData<List<FavoriteUser>> = userRepository.getFavoriteUser()
}