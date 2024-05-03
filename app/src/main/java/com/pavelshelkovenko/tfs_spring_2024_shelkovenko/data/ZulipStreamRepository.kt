package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.utils.toStream
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.utils.toTopic
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Stream
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.StreamDestination
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.repository.StreamRepository
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.containsQuery
import javax.inject.Inject

class ZulipStreamRepository @Inject constructor(
    private val zulipApi: ZulipApi
): StreamRepository {
    override suspend fun getAllStreams(): List<Stream> {
        val streamsDto = zulipApi.getAllStreams().allStreams
        val streams = streamsDto.map { streamDto ->
            streamDto.toStream()
        }
        val streamsWithTopics = streams.map { stream ->
            val topicsDto = zulipApi.getTopics(stream.id).topics
            val topics = topicsDto.map { topicDto ->
                topicDto.toTopic()
            }
            stream.copy(topicsList = topics)
        }
        return streamsWithTopics
    }

    override suspend fun getSubscribedStreams(): List<Stream> {
        val streamsDto = zulipApi.getSubscribedStreams().subscribedStreams
        val streams = streamsDto.map { streamDto ->
            streamDto.toStream()
        }
        val streamsWithTopics = streams.map { stream ->
            val topicsDto = zulipApi.getTopics(stream.id).topics
            val topics = topicsDto.map { topicDto ->
                topicDto.toTopic()
            }
            stream.copy(topicsList = topics)
        }
        return streamsWithTopics
    }

    override suspend fun searchStreams(query: String, streamDestination: StreamDestination): List<Stream> {
        return when(streamDestination) {
            StreamDestination.ALL -> {
                getAllStreams().filter { it.name.containsQuery(query) }
            }
            StreamDestination.SUBSCRIBED -> {
                getSubscribedStreams().filter { it.name.containsQuery(query) }
            }
        }
    }

}