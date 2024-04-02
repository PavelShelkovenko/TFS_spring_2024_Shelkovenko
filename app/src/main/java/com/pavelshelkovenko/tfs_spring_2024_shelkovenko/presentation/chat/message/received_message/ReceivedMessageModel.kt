package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message.received_message

import android.graphics.Bitmap
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message.reaction.Reaction

data class ReceivedMessageModel(
    val userId: Int,
    val userAvatar: Bitmap,
    val userName: String,
    val textMessage: String,
    val reactionList: List<Reaction>
)