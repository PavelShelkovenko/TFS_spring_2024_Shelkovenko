package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.channels.streams

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Stream
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.StreamDestination
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Topic
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.channels.streams.adapter.StreamDelegateItem

sealed interface StreamEvent {

    sealed interface Ui: StreamEvent {
        data class StartProcess(val streamDestination: StreamDestination): Ui

        data class QueryChanged(val newQuery: String, val streamDestination: StreamDestination): Ui

        data class OnStreamClick(
            val stream: StreamDelegateItem,
            val streamDestination: StreamDestination
        ): Ui

        data class OnTopicClick(
            val topic: Topic,
            val streamDestination: StreamDestination
        ): Ui

        data class ReloadData(val streamDestination: StreamDestination): Ui
    }

    sealed interface Internal: StreamEvent {

        data class DataLoaded(
            val streams: List<Stream>,
            val streamDestination: StreamDestination
        ): Internal

        data class DataLoadedFromCache(
            val streams: List<Stream>,
            val streamDestination: StreamDestination
        ): Internal

        data class TopicsLoaded(
            val topics: List<Topic>,
            val stream: StreamDelegateItem,
            val streamDestination: StreamDestination
        ): Internal

        data class Error(val throwable: Throwable): Internal

        data class MinorError(val errorMessageId: Int): Internal
    }
}