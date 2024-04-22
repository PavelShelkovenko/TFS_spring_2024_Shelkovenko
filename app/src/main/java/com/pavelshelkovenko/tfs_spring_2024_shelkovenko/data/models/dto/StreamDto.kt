package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.models.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class StreamDto(
    @SerializedName("stream_id")
    val id: Int,
    @SerializedName("name")
    val streamName: String,
): Serializable