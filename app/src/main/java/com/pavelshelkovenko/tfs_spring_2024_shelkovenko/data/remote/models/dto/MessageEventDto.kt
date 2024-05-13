package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.dto

import com.google.gson.annotations.SerializedName

data class MessageEventDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("message")
    val message: MessageDto,
)