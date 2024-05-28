package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.response

import com.google.gson.annotations.SerializedName

data class StreamIdResponse(
    @SerializedName("stream_id")
    val streamId: Int
)
