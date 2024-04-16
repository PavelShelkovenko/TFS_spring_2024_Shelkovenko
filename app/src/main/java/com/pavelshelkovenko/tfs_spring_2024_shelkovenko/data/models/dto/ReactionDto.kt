package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.models.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ReactionDto(
    @SerializedName("emoji_code")
    val emojiCode: String,
    @SerializedName("emoji_name")
    val emojiName: String,
    @SerializedName("user_id")
    var userId: Int,
): Serializable