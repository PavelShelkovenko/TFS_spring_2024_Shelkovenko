package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.response

import com.google.gson.annotations.SerializedName
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.dto.MessageEventDto

data class GetMessageEventResponse(
    @SerializedName("events")
    val messageEvents: List<MessageEventDto>
)