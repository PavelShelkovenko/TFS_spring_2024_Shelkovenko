package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.models

import android.graphics.Bitmap

data class User(
    val id: Int,
    val avatar: Bitmap,
    val name: String,
    val email: String,
    val onlineStatus: UserOnlineStatus,
    val activityStatus: String,
)

enum class UserOnlineStatus {
    ACTIVE, IDLE, OFFLINE,
}