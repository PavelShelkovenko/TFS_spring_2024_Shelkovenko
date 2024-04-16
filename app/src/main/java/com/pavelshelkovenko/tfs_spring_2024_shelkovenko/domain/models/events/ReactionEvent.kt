package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events

data class ReactionEvent(
    val emojiCode: String,
    val emojiName: String,
    val operation: Operation,
    val messageId: Int,
    val userId: Int,
)

enum class Operation {
    ADD, REMOVE
}
