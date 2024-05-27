package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models

data class AccountSettings(
    val userId: Int = 708822,
    val email: String = "pavel.shelkovenko@gmail.com",
    val userName: String = "",
    val emailVisibility: EmailVisibility = EmailVisibility.EVERYONE,
    val isInvisibleMode: Boolean = false,
    val avatarUrl: String = "https://zulip-avatars.s3.amazonaws.com/63414/9e5d147143cdc4f9d5b88e649a7185376201b312?version=4",
)

enum class EmailVisibility {
    EVERYONE, MEMBERS, ADMINS, NOBODY
}