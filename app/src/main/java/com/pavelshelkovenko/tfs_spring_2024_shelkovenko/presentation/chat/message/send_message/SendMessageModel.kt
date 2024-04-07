package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message.send_message

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message.reaction.Reaction

data class SendMessageModel(
    val userId: Int,
    val textMessage: String,
    val reactionList: List<Reaction>
)
