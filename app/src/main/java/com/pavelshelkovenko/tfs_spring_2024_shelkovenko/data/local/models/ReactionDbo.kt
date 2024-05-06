package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models

data class ReactionDbo(
    val emojiCode: String,
    val emojiName: String,
    var userId: Int,
    val count: Int,
)