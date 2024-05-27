package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.AppDatabase
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.EmailVisibility

@Entity(
    tableName = AppDatabase.ACCOUNT_SETTINGS_TABLE_NAME,
    indices = [Index(value = ["userId"], unique = true)]
)
data class AccountSettingsDbo(
    @PrimaryKey
    val userId: Int = 708822,
    val userName: String = "Pavel Shelkovenko",
    val email: String = "pavel.shelkovenko@gmail.com",
    val emailVisibility: EmailVisibility = EmailVisibility.EVERYONE,
    val isInvisibleMode: Boolean = false,
    val avatarUrl: String = "https://zulip-avatars.s3.amazonaws.com/63414/9e5d147143cdc4f9d5b88e649a7185376201b312?version=4",
)
