package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.dto

import com.google.gson.annotations.SerializedName

data class TopicDto(
    @SerializedName("max_id")
    val lastMessageId: Int,
    @SerializedName("name")
    val topicName: String,
)