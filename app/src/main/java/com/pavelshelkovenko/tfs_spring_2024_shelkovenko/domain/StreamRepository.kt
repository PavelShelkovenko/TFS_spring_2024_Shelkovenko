package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Stream
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.StreamDestination

interface StreamRepository {

    suspend fun getAllStreams(): List<Stream>

    suspend fun getSubscribedStreams(): List<Stream>

    suspend fun searchStreams(query: String, streamDestination: StreamDestination): List<Stream>
}