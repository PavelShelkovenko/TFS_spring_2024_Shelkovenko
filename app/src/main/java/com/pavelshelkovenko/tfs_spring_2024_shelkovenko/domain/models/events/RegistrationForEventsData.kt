package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events

data class RegistrationForEventsData(
    val messagesQueueId: String,
    val messageLastEventId: String,
    val reactionsQueueId: String,
    val reactionLastEventId: String
)
