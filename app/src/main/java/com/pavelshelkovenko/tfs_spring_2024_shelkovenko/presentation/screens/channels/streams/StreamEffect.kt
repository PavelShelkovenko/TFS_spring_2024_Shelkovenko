package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.channels.streams

sealed interface StreamEffect {

    data class NavigateToChat(val topicName: String, val streamName: String) : StreamEffect

    data class MinorError(val errorMessageId: Int) : StreamEffect
}