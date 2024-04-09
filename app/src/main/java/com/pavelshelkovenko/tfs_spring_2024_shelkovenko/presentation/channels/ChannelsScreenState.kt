package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.delegate_adapter.DelegateItem

sealed interface ChannelsScreenState {
    data object Initial : ChannelsScreenState

    data object Loading : ChannelsScreenState

    data class Error(val errorMessage: String) : ChannelsScreenState

    data class Content(
        val streamsList: List<DelegateItem>,
    ) : ChannelsScreenState
}