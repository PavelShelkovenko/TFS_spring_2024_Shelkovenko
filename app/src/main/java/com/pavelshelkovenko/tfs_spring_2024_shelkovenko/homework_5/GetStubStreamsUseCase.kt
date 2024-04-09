package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.homework_5

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.delegate_adapter.DelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.generateRandomColor
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.generateRandomName
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.streams.Stream
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.streams.StreamDelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.streams.StreamDestination
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.topics.Topic
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.throwRandomError
import kotlinx.coroutines.delay
import kotlin.random.Random

class GetStubStreamsUseCase {

    private val stubTopic: Topic
        get() = Topic(
            id = Random.nextInt(0, 1000000),
            name = generateRandomName(),
            messageCount = Random.nextInt(0, 1000),
            color = generateRandomColor()
        )

    private val stubTopicList: List<Topic>
        get() {
            val newList = mutableListOf<Topic>()
            for (i in 0..3) {
                newList.add(stubTopic)
            }
            return newList
        }

    private val stubSubscribedStream: Stream
        get() = Stream(
            id = Random.nextInt(0, 1000000),
            name = generateRandomName(),
            topicsList = stubTopicList
        )

    private val stubAllStream: Stream
        get() = Stream(
            id = Random.nextInt(0, 1000000),
            name = generateRandomName(),
            topicsList = stubTopicList
        )

    private val stubSubscribedStreamDelegateItem: StreamDelegateItem
        get() = StreamDelegateItem(
            id = Random.nextInt(0, 1000000),
            value = stubSubscribedStream
        )

    private val stubAllStreamDelegateItem: StreamDelegateItem
        get() = StreamDelegateItem(
            id = Random.nextInt(0, 1000000),
            value = stubAllStream
        )


    private val stubSubscribedStreamsList = mutableListOf<DelegateItem>()
    private val stubAllStreamsList = mutableListOf<DelegateItem>()


    init {
        for (i in 0..30) {
            stubAllStreamsList.add(stubAllStreamDelegateItem)
            stubSubscribedStreamsList.add(stubSubscribedStreamDelegateItem)
        }
    }

    suspend fun search(query: String, streamDestination: StreamDestination): List<DelegateItem> {
        delay(500L)
        throwRandomError()
        return when(streamDestination) {
            StreamDestination.AllStreams -> {
                if (query.isEmpty()) {
                    stubAllStreamsList
                } else {
                    stubAllStreamsList.filter { (it.content() as Stream).name.lowercase().contains(query) }
                }
            }
            StreamDestination.Subscribed -> {
                if (query.isEmpty()) {
                    stubSubscribedStreamsList
                } else {
                    stubSubscribedStreamsList.filter { (it.content() as Stream).name.lowercase().contains(query) }
                }

            }
        }
    }

    suspend operator fun invoke(streamDestination: StreamDestination): List<DelegateItem> {
        delay(1000)
        throwRandomError()
        return when(streamDestination) {
            StreamDestination.AllStreams -> {
                stubAllStreamsList
            }
            StreamDestination.Subscribed -> {
                stubSubscribedStreamsList
            }
        }
    }
}