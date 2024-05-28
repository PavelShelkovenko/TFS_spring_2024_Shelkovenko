package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.channels.streams

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.base.delegate_adapter.DelegateItem


sealed interface StreamState {

    data object Initial : StreamState

    data object Loading : StreamState

    data class Error(val errorMessageId: Int) : StreamState

    data class Content(
        val allStreamsList: List<DelegateItem>,
        val subscribedStreamsList: List<DelegateItem>
    ) : StreamState
}