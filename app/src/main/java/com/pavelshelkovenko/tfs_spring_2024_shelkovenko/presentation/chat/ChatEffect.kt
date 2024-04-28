package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat

sealed interface ChatEffect {

    data class MinorError(val errorMessage: String): ChatEffect

    data object NewMessageReceived: ChatEffect
}