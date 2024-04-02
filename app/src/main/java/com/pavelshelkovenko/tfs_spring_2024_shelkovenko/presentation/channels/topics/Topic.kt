package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.topics

data class Topic(
    val id: Int,
    val name: String,
    val messageCount: Int,
    val color: Int,
)
