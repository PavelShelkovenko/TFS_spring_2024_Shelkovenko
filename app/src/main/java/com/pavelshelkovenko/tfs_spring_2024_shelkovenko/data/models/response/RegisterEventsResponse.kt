package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.models.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RegisterEventsResponse(
    @SerializedName("queue_id")
    val queueId: String,
    @SerializedName("last_event_id")
    val lastEventId: String
): Serializable