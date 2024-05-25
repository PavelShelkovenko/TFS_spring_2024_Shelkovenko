package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.response

import com.google.gson.annotations.SerializedName
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.dto.StreamDto

data class GetStreamByIdResponse(
    @SerializedName("stream")
    val stream: StreamDto
)
