package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models

data class User(
    val id: Int,
    val avatarUrl: String,
    val name: String,
    val email: String,
    val onlineStatus: UserOnlineStatus = UserOnlineStatus.OFFLINE,
)

enum class UserOnlineStatus {
    ACTIVE, IDLE, OFFLINE,
}