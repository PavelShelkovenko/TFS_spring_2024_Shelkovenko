package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.response

import com.google.gson.annotations.SerializedName
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.dto.StreamDto
import java.io.Serializable


data class GetAllStreamsResponse(
    @SerializedName("streams")
    val allStreams: List<StreamDto>
): Serializable