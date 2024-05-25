package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.channels.streams

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.StreamDestination
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.channels.streams.adapter.StreamDelegateItem

sealed interface StreamCommand {

    data class LoadDataFromNetwork(val streamDestination: StreamDestination): StreamCommand

    data class LoadDataFromCache(val streamDestination: StreamDestination): StreamCommand

    data class LoadTopicsForStream(
        val stream: StreamDelegateItem,
        val streamDestination: StreamDestination
    ): StreamCommand

    data class ProcessSearch(
        val query: String,
        val streamDestination: StreamDestination
    ): StreamCommand

    data class CreateStream(val streamName: String): StreamCommand
}