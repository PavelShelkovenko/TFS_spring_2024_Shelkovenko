package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models

data class Topic(
    val id: Int,
    val name: String,
    val lastMessageId: Int,
    val color: Int,
)
