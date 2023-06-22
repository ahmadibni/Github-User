package com.example.githubuser.utils

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val login: String,
    val avatarUrl: String,
    val followingUrl: String,
    val followerUrl: String,
    val url: String
) : Parcelable
