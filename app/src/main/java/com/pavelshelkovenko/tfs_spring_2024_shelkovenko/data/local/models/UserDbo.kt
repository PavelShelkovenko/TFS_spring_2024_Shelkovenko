package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.AppDatabase
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.UserOnlineStatus

@Entity(tableName = AppDatabase.USERS_TABLE_NAME)
data class UserDbo(
    @PrimaryKey
    val id: Int,
    val name: String,
    val avatarUrl: String,
    val email: String,
    val onlineStatus: UserOnlineStatus,
)
