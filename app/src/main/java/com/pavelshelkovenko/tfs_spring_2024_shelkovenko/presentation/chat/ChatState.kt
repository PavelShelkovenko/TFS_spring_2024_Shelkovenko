package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.delegate_adapter.DelegateItem

sealed interface ChatState {
    data object Initial : ChatState

    data object Loading : ChatState

    data class Error(val errorMessage: String) : ChatState

    data class Content(val messages: List<DelegateItem>) : ChatState
}