package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.response

import com.google.gson.annotations.SerializedName

data class GetOwnProfileResponse(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("delivery_email")
    val email: String?,
    @SerializedName("email")
    val zulipEmail: String,
    @SerializedName("full_name")
    val userName: String,
)