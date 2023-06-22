package com.example.githubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.githubuser.data.local.entity.FavoriteUser

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: FavoriteUser)

    @Query("SELECT * FROM user")
    fun getFavoriteUser(): LiveData<List<FavoriteUser>>

    @Query("DELETE FROM user WHERE username = :username")
    fun delete(username: String)

    @Query("SELECT * FROM user WHERE username = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser>
}