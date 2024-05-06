package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.repository

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.dao.StreamDao
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models.SubscriptionStatus
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.ZulipApi
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.utils.toStreamDbo
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.utils.toStreamDomain
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.utils.toTopicDomain
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Stream
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.StreamDestination
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.repository.StreamRepository
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.containsQuery
import javax.inject.Inject

class StreamRepositoryImpl @Inject constructor(
    private val zulipApi: ZulipApi,
    private val streamDao: StreamDao,
) : StreamRepository {

    override suspend fun getStreamsByDestination(streamDestination: StreamDestination): List<Stream> {
        val streamsDto = when (streamDestination) {
            StreamDestination.ALL -> zulipApi.getAllStreams().allStreams
            StreamDestination.SUBSCRIBED -> zulipApi.getSubscribedStreams().subscribedStreams
        }
        val streams = streamsDto.map { streamDto ->
            streamDto.toStreamDomain()
        }
        val streamsWithTopics = streams.map { stream ->
            val topicsDto = zulipApi.getTopics(stream.id).topics
            val topics = topicsDto.map { topicDto ->
                topicDto.toTopicDomain()
            }
            stream.copy(topicsList = topics)
        }
        saveStreamsInCache(streamsWithTopics, streamDestination)
        return streamsWithTopics.sortedBy { it.id }
    }

    override suspend fun searchStreams(
        query: String,
        streamDestination: StreamDestination
    ): List<Stream> =
        getStreamsByDestination(streamDestination).filter { it.name.containsQuery(query) }

    override suspend fun getStreamsByDestinationFromCache(streamDestination: StreamDestination): List<Stream> {
        val cachedStreams = when (streamDestination) {
            StreamDestination.ALL -> streamDao.getAllStreams()
            StreamDestination.SUBSCRIBED -> streamDao.getSubscribedStreams(SubscriptionStatus.SUBSCRIBED.name)
        }
        return when(streamDestination) {
            StreamDestination.ALL -> {
                val allIsSubscribed =
                    cachedStreams.all { it.subscriptionStatus == SubscriptionStatus.SUBSCRIBED }
                if (allIsSubscribed) {
                    emptyList()
                } else {
                    cachedStreams.map { streamDbo -> streamDbo.toStreamDomain() }
                }
            }
            StreamDestination.SUBSCRIBED -> {
                cachedStreams.map { streamDbo -> streamDbo.toStreamDomain() }
            }
        }
    }

    private suspend fun saveStreamsInCache(
        streams: List<Stream>,
        streamDestination: StreamDestination
    ) {
        when (streamDestination) {
            StreamDestination.ALL -> {
                val streamsForCaching = streams.map {
                    it.toStreamDbo(subscriptionStatus = SubscriptionStatus.UNSUBSCRIBED)
                }
                for (stream in streamsForCaching) {
                    streamDao.insert(stream)
                }
            }

            StreamDestination.SUBSCRIBED -> {
                val streamsForCaching = streams.map {
                    it.toStreamDbo(subscriptionStatus = SubscriptionStatus.SUBSCRIBED)
                }
                for (stream in streamsForCaching) {
                    streamDao.insert(stream)
                }
            }
        }
    }
}