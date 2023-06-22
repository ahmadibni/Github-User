package com.example.githubuser.data.local

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubuser.data.local.entity.FavoriteUser
import com.example.githubuser.data.local.room.UserDao
import com.example.githubuser.data.local.room.UserDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository(application: Application){
    private val userDao: UserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = UserDatabase.getDatabase(application)
        userDao = db.userDao()
    }

    fun getFavoriteUser(): LiveData<List<FavoriteUser>> = userDao.getFavoriteUser()

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> = userDao.getFavoriteUserByUsername(username)

    fun setFavoriteUser(user: FavoriteUser){
        executorService.execute {
            userDao.insert(user)
        }
    }

    fun delete(username: String){
        executorService.execute { userDao.delete(username) }
    }
}