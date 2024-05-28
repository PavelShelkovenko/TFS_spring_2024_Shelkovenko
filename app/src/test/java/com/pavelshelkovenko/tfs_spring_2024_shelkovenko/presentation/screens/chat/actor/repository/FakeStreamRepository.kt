package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.actor.repository

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Stream
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.StreamDestination
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Topic
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.repository.StreamRepository

class FakeStreamRepository: StreamRepository {
    override suspend fun getStreamsByDestinationFromNetwork(streamDestination: StreamDestination): List<Stream> {
        TODO("Not yet implemented")
    }

    override suspend fun getStreamsByDestinationFromCache(streamDestination: StreamDestination): List<Stream> {
        TODO("Not yet implemented")
    }

    override suspend fun searchStreams(
        query: String,
        streamDestination: StreamDestination
    ): List<Stream> {
        TODO("Not yet implemented")
    }

    override suspend fun searchStreamsInCache(
        query: String,
        streamDestination: StreamDestination
    ): List<Stream> {
        TODO("Not yet implemented")
    }

    override suspend fun createStream(streamName: String): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getTopicsForStreamById(streamId: Int): List<Topic> {
        TODO("Not yet implemented")
    }

    override suspend fun subscribeToStream(streamName: String, streamId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun unsubscribeFromStream(streamName: String, streamId: Int) {
        TODO("Not yet implemented")
    }
}