package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.streams

sealed interface StreamEffect {
    data class OnTopicClick(val topicName: String, val streamName: String) : StreamEffect
}