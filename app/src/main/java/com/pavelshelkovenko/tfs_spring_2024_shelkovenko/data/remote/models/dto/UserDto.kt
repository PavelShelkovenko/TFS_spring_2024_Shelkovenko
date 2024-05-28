package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.dto

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("user_id")
    val id: Int,
    @SerializedName("full_name")
    val userName: String,
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("delivery_email")
    val email: String?,
    @SerializedName("email")
    val zulipEmail: String,
)