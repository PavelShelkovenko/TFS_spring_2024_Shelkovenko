package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels

import androidx.lifecycle.ViewModel
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.delegate_adapter.DelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.generateRandomColor
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.generateRandomName
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.streams.Stream
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.streams.StreamDelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.streams.StreamsInfoFragment
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.topics.Topic
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.topics.TopicDelegateItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random


class ChannelsViewModel : ViewModel() {

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


    private val stubSubscribedStreamsList: List<DelegateItem>
        get() {
            val newList = mutableListOf<DelegateItem>()
            for (i in 0..30) {
                newList.add(stubSubscribedStreamDelegateItem)
            }
            return newList
        }

    private val stubAllStreamsList: List<DelegateItem>
        get() {
            val newList = mutableListOf<DelegateItem>()
            for (i in 0..30) {
                newList.add(stubAllStreamDelegateItem)
            }
            return newList
        }


    private val _allStreamsList = MutableStateFlow(stubAllStreamsList)
    val allStreamsList = _allStreamsList.asStateFlow()

    private val _subscribedStreamsList = MutableStateFlow(stubSubscribedStreamsList)
    val subscribedStreamsList = _subscribedStreamsList.asStateFlow()


    fun onStreamClick(stream: StreamDelegateItem, streamDestination: String) {
        val streamModel = stream.content() as Stream
        when (streamDestination) {
            StreamsInfoFragment.ALL_STREAMS_DESTINATION -> {
                if (streamModel.isExpanded) {
                    val newListWithoutTopics = deleteTopics(
                        listWhereDeleteTopics = _allStreamsList.value,
                        stream = stream,
                        streamModel = streamModel
                    )
                    _allStreamsList.value = newListWithoutTopics
                } else {
                    val newListWithTopics = addTopics(
                        listWhereAddTopics = _allStreamsList.value,
                        stream = stream,
                        streamModel = streamModel
                    )
                    _allStreamsList.value = newListWithTopics
                }
            }

            StreamsInfoFragment.SUBSCRIBED_STREAMS_DESTINATION -> {
                if (streamModel.isExpanded) {
                    val newListWithoutTopics = deleteTopics(
                        listWhereDeleteTopics = _subscribedStreamsList.value,
                        stream = stream,
                        streamModel = streamModel
                    )
                    _subscribedStreamsList.value = newListWithoutTopics
                } else {
                    val newListWithTopics = addTopics(
                        listWhereAddTopics = _subscribedStreamsList.value,
                        stream = stream,
                        streamModel = streamModel
                    )
                    _subscribedStreamsList.value = newListWithTopics
                }
            }
        }
    }

    private fun addTopics(
        listWhereAddTopics: List<DelegateItem>,
        stream: StreamDelegateItem,
        streamModel: Stream
    ): List<DelegateItem> {
        val newList = mutableListOf<DelegateItem>()
        for (delegateItem in listWhereAddTopics) {
            if (stream.id != delegateItem.id()) {
                newList.add(delegateItem)
            } else {
                val newStreamDelegateItem = StreamDelegateItem(
                    id = stream.id,
                    value = streamModel.copy(isExpanded = true)
                )
                newList.add(newStreamDelegateItem)
                for (topic in streamModel.topicsList) {
                    val topicDelegateItem = TopicDelegateItem(id = topic.id, value = topic)
                    newList.add(topicDelegateItem)
                }
            }
        }
        return newList
    }

    private fun deleteTopics(
        listWhereDeleteTopics: List<DelegateItem>,
        stream: StreamDelegateItem,
        streamModel: Stream
    ): List<DelegateItem> {
        val newList = listWhereDeleteTopics.toMutableList()
        val streamIndex = newList.indexOf(stream)
        val streamTopics = streamModel.topicsList
        val lastTopicIndex = streamIndex + streamTopics.size
        val newStreamDelegateItem = StreamDelegateItem(
            id = stream.id,
            value = streamModel.copy(isExpanded = false)
        )
        newList.removeAt(streamIndex)
        newList.add(streamIndex, newStreamDelegateItem)
        for (i in streamTopics.indices) {
            newList.removeAt(lastTopicIndex - i)
        }
        return newList
    }


    fun findStreamByItsTopic(topic: Topic, streamDestination: String): Stream {
        var result: Stream? = null
        when (streamDestination) {
            StreamsInfoFragment.ALL_STREAMS_DESTINATION -> {
                result = findStreamByItsTopicInListSource(_allStreamsList.value, topic)
            }

            StreamsInfoFragment.SUBSCRIBED_STREAMS_DESTINATION -> {
                result = findStreamByItsTopicInListSource(_subscribedStreamsList.value, topic)
            }
        }
        return result ?: throw IllegalStateException("Invalid stream")
    }

    private fun findStreamByItsTopicInListSource(listWhereToFind: List<DelegateItem>, topic: Topic): Stream? {
        var result: Stream? = null
        listWhereToFind.forEach { delegateItem ->
            try {
                val resultTopic =
                    ((delegateItem as StreamDelegateItem).content() as Stream).topicsList.find {
                        it.id == topic.id
                    }
                if (resultTopic != null) {
                    result = (delegateItem.content() as Stream)
                }
            } catch (_: Exception) { }
        }
        return result
    }
}