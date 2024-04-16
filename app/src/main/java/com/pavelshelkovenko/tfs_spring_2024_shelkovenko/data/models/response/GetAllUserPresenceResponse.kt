package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.models.response

import com.google.gson.annotations.SerializedName
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.models.dto.PresenceDto
import java.io.Serializable

data class GetAllUserPresenceResponse(
    @SerializedName("presences")
    val presences: Map<String, PresenceDto>
): Serializable