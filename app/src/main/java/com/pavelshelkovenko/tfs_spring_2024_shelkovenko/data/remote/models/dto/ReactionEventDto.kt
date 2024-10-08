package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.dto

import com.google.gson.annotations.SerializedName

data class ReactionEventDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("op")
    val operationDto: OperationDto,
    @SerializedName("emoji_code")
    val emojiCode: String,
    @SerializedName("emoji_name")
    val emojiName: String,
    @SerializedName("message_id")
    val messageId: Int,
    @SerializedName("user_id")
    val userId: Int,
)

enum class OperationDto {
    @SerializedName("add")
    ADD,

    @SerializedName("remove")
    REMOVE
}