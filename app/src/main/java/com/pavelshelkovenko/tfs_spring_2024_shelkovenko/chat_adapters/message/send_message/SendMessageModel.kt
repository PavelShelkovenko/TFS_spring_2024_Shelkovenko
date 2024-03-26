package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.chat_adapters.message.send_message

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.models.Reaction

data class SendMessageModel(
    val userId: Int,
    val textMessage: String,
    val reactionList: List<Reaction>
)
