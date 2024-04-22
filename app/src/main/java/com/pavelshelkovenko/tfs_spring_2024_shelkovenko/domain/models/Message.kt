package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models

data class Message(
    val id: Int,
    val avatarUrl: String,
    val message: String,
    val userId: Int,
    val userName: String,
    val dateInUTCSeconds: Int,
    val reactions: List<Reaction>,
)
