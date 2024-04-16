package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Message

data class ReceivedMessageEventData(
    val newLastEventId: String,
    val newMessages: List<Message>
)
