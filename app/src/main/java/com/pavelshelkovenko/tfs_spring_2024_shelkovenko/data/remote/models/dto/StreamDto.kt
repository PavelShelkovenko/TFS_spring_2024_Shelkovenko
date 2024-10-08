package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.dto

import com.google.gson.annotations.SerializedName

data class StreamDto(
    @SerializedName("stream_id")
    val id: Int,
    @SerializedName("name")
    val streamName: String,
)