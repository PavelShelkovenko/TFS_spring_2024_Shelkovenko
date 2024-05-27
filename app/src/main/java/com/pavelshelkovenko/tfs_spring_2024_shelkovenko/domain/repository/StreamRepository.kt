package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.repository

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Stream
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.StreamDestination
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Topic

interface StreamRepository {

    suspend fun getStreamsByDestinationFromNetwork(streamDestination: StreamDestination): List<Stream>

    suspend fun getStreamsByDestinationFromCache(streamDestination: StreamDestination): List<Stream>

    suspend fun searchStreams(query: String, streamDestination: StreamDestination): List<Stream>

    suspend fun createStream(streamName: String): Int

    suspend fun getTopicsForStreamById(streamId: Int): List<Topic>

    suspend fun subscribeToStream(streamName: String, streamId: Int)

    suspend fun unsubscribeFromStream(streamName: String, streamId: Int)
}