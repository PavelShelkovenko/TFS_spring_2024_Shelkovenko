package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.base.delegate_adapter.DelegateItem


sealed interface ChatEffect {

    data class MinorError(val errorMessageId: Int): ChatEffect

    data object NewMessageReceived: ChatEffect

    data object CloseChat: ChatEffect

    data class OpenTopicChooser(val topics: List<DelegateItem>): ChatEffect

    data object DeactivateEditingMode: ChatEffect
}