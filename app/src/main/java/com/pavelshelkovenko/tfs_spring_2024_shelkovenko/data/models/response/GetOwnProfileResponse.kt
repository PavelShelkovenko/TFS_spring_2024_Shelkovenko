package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.models.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetOwnProfileResponse(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("full_name")
    val userName: String,
): Serializable