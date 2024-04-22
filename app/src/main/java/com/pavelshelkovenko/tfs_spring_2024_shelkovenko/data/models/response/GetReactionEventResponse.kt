package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.models.response

import com.google.gson.annotations.SerializedName
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.models.dto.ReactionEventDto
import java.io.Serializable

data class GetReactionEventResponse(
    @SerializedName("events")
    val reactionEvents: List<ReactionEventDto>
): Serializable