package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.chat_adapters.message.received_message

import android.graphics.Bitmap
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.models.Reaction

data class ReceivedMessageModel(
    val userId: Int,
    val userAvatar: Bitmap,
    val userName: String,
    val textMessage: String,
    val reactionList: List<Reaction>
)