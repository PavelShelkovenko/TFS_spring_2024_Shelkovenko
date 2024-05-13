package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.message.received_message

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Reaction

data class ReceivedMessageModel(
    val userId: Int,
    val avatarUrl: String,
    val userName: String,
    val textMessage: String,
    val reactionList: List<Reaction>,
    val dateInUTCSeconds: Int,
)