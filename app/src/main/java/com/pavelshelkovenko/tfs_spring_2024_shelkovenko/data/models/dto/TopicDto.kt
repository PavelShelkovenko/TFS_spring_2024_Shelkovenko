package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.models.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TopicDto(
    @SerializedName("max_id")
    val lastMessageId: Int,
    @SerializedName("name")
    val topicName: String,
): Serializable