package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.streams

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.delegate_adapter.DelegateItem


sealed interface StreamState {

    data object Initial : StreamState

    data object Loading : StreamState

    data class Error(val errorMessage: String) : StreamState

    data class Content(
        val allStreamsList: List<DelegateItem>,
        val subscribedStreamsList: List<DelegateItem>
    ) : StreamState
}