package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.channels.streams

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.StreamDestination

sealed interface StreamCommand {

    data class LoadData(val streamDestination: StreamDestination): StreamCommand

    data class LoadDataFromCache(val streamDestination: StreamDestination): StreamCommand

    data class ProcessSearch(val query: String, val streamDestination: StreamDestination):
        StreamCommand
}