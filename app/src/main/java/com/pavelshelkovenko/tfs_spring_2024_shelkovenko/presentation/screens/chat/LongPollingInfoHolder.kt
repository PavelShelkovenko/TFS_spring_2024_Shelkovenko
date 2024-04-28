package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat

data class LongPollingInfoHolder(
    var messageQueueId: String = "",
    var reactionQueueId: String = "",
    var lastMessageEventId: String = "",
    var lastReactionEventId: String = "",
)