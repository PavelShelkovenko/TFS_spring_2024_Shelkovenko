package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.models.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserDto(
    @SerializedName("user_id")
    val id: Int,
    @SerializedName("full_name")
    val userName: String,
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("is_bot")
    val isBot: Boolean = false,
    var presence: UserOnlineStatusDto = UserOnlineStatusDto.OFFLINE,
): Serializable