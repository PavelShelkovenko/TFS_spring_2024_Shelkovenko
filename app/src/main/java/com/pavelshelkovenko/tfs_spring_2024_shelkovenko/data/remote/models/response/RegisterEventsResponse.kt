package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.response

import com.google.gson.annotations.SerializedName

data class RegisterEventsResponse(
    @SerializedName("queue_id")
    val queueId: String,
    @SerializedName("last_event_id")
    val lastEventId: String
)