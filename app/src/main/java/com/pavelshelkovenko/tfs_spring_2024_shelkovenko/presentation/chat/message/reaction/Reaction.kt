package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message.reaction

data class Reaction(
    val userId: Int,
    val emojiCode: String,
    val count: Int
)