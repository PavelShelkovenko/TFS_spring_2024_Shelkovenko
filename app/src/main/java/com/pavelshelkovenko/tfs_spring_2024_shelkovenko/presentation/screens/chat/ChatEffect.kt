package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat

sealed interface ChatEffect {

    data class MinorError(val errorMessageId: Int): ChatEffect

    data object NewMessageReceived: ChatEffect

    data object CloseChat: ChatEffect
}