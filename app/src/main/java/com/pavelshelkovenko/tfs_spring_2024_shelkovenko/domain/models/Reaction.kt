package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models

data class Reaction(
    val userId: Int,
    val emojiCode: String,
    val emojiName: String,
    val count: Int
)