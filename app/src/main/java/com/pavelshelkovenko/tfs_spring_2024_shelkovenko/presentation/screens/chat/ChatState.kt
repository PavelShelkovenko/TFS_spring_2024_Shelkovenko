package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.base.delegate_adapter.DelegateItem

sealed interface ChatState {

    data object Initial : ChatState

    data object Loading : ChatState

    data class Error(val errorMessageId: Int) : ChatState

    data class Content(
        val messages: List<DelegateItem>,
        val editModeState: EditModeState
    ) : ChatState
}