package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.delegate_adapter.DelegateItem

sealed interface ChatScreenState {
    data object Initial : ChatScreenState

    data object Loading : ChatScreenState

    data class Error(val errorMessage: String) : ChatScreenState

    data class Content(val messages: List<DelegateItem>) : ChatScreenState
}