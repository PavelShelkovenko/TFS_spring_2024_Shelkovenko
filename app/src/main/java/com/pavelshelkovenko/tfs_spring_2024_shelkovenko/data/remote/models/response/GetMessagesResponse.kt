package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.response

import com.google.gson.annotations.SerializedName
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.dto.MessageDto
import java.io.Serializable

data class GetMessagesResponse(
    @SerializedName("messages")
    val messages: List<MessageDto>
): Serializable