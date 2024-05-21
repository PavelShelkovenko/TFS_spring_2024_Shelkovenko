package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.repository

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.dao.StreamDao
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.dao.TopicDao
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models.SubscriptionStatus
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.mappers.toStreamDbo
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.mappers.toStreamDomain
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.mappers.toTopicDbo
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.mappers.toTopicDomainList
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.ZulipApi
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Stream
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.StreamDestination
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Topic
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.repository.StreamRepository
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.containsQuery
import javax.inject.Inject

class StreamRepositoryImpl @Inject constructor(
    private val zulipApi: ZulipApi,
    private val streamDao: StreamDao,
    private val topicDao: TopicDao,
) : StreamRepository {

    override suspend fun getStreamsByDestination(streamDestination: StreamDestination): List<Stream> {
        val streamsDto = when (streamDestination) {
            StreamDestination.ALL -> zulipApi.getAllStreams().allStreams
            StreamDestination.SUBSCRIBED -> zulipApi.getSubscribedStreams().subscribedStreams
        }
        val streams = streamsDto.map { streamDto ->
            streamDto.toStreamDomain()
        }
        saveStreamsInCache(streams, streamDestination)
        return streams.sortedBy { it.id }
    }

    override suspend fun searchStreams(
        query: String,
        streamDestination: StreamDestination
    ): List<Stream> =
        getStreamsByDestination(streamDestination).filter { it.name.containsQuery(query) }

    override suspend fun getTopicsForStream(streamId: Int): List<Topic> {
        deleteOldTopics()
        val topicsDbo = topicDao.getTopicsForStream(streamId)
        return if (topicsDbo.isNotEmpty()) {
            topicsDbo.toTopicDomainList()
        } else {
            getTopicsFromNetwork(streamId)
        }
    }

    override suspend fun getStreamsByDestinationFromCache(streamDestination: StreamDestination): List<Stream> {
        val cachedStreams = when (streamDestination) {
            StreamDestination.ALL -> streamDao.getAllStreams()
            StreamDestination.SUBSCRIBED -> streamDao.getSubscribedStreams(SubscriptionStatus.SUBSCRIBED.name)
        }
        return when (streamDestination) {
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

    private suspend fun deleteOldTopics() {
        val currentTime = System.currentTimeMillis()
        val timeThreshold = currentTime - 5 * 60 * 1000
        topicDao.deleteOldTopics(timeThreshold)
    }

    private suspend fun getTopicsFromNetwork(streamId: Int): List<Topic> {
        val topicsDbo = zulipApi.getTopics(streamId).topics.map { topicDto -> topicDto.toTopicDbo(streamId) }
        topicDao.insertAll(topics = topicsDbo)
        return topicsDbo.toTopicDomainList().sortedBy { it.id }
    }
}