package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.dto

import com.google.gson.annotations.SerializedName

data class ReactionDto(
    @SerializedName("emoji_code")
    val emojiCode: String,
    @SerializedName("emoji_name")
    val emojiName: String,
    @SerializedName("user_id")
    var userId: Int,
)