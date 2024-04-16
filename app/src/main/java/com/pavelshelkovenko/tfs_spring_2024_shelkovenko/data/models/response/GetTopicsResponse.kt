package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.models.response

import com.google.gson.annotations.SerializedName
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.models.dto.TopicDto
import java.io.Serializable

data class GetTopicsResponse(
    @SerializedName("topics")
    val topics: List<TopicDto>
): Serializable
