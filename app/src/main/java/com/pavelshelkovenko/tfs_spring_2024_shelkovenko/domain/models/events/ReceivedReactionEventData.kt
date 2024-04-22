package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events

data class ReceivedReactionEventData(
    val newLastEventId: String,
    val reactionEvents: List<ReactionEvent>
)

