package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.response

import com.google.gson.annotations.SerializedName
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.dto.PresenceDto

data class GetUserPresenceResponse(
    @SerializedName("presence")
    val presence: PresenceDto
)