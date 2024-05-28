package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.dto

import com.google.gson.annotations.SerializedName

data class MessageDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("content")
    val message: String,
    @SerializedName("sender_id")
    val userId: Int,
    @SerializedName("sender_full_name")
    val userName: String,
    @SerializedName("timestamp")
    val dateInUTCSeconds: Int,
    @SerializedName("reactions")
    val reactions: List<ReactionDto>,
)