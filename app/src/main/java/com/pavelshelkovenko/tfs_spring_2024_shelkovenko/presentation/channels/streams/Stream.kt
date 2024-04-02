package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.streams

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.topics.Topic

data class Stream(
    val id: Int,
    val name: String,
    val isExpanded: Boolean = false,
    val topicsList: List<Topic>,
)
