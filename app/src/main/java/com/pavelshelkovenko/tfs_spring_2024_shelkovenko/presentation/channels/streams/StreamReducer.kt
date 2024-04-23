package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.streams

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Stream
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.StreamDestination
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Topic
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.generateRandomId
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.streams.adapter.StreamDelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.topics.TopicDelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.delegate_adapter.DelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.toDelegateList
import kotlinx.coroutines.flow.MutableStateFlow
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer

class StreamReducer : ScreenDslReducer<
        StreamEvent,
        StreamEvent.Ui,
        StreamEvent.Internal,
        StreamState,
        StreamEffect,
        StreamCommand>
    (StreamEvent.Ui::class, StreamEvent.Internal::class) {

    private val allStreamsListCached = MutableStateFlow<List<DelegateItem>>(emptyList())
    private val subscribedStreamsListCached = MutableStateFlow<List<DelegateItem>>(emptyList())

    override fun Result.internal(event: StreamEvent.Internal) = when (event) {

        is StreamEvent.Internal.DataLoaded -> state {
            when (event.streamDestination) {
                StreamDestination.ALL -> {
                    allStreamsListCached.value = event.streams.toDelegateList()
                    StreamState.Content(
                        allStreamsList = allStreamsListCached.value,
                        subscribedStreamsList = subscribedStreamsListCached.value
                    )
                }
                StreamDestination.SUBSCRIBED -> {
                    subscribedStreamsListCached.value = event.streams.toDelegateList()
                    StreamState.Content(
                        allStreamsList = allStreamsListCached.value,
                        subscribedStreamsList = subscribedStreamsListCached.value
                    )
                }
            }
        }

        is StreamEvent.Internal.Error -> state {
            StreamState.Error(errorMessage = event.throwable.message.toString())
        }
    }

    override fun Result.ui(event: StreamEvent.Ui) = when (event) {

        is StreamEvent.Ui.Init -> {
            state { StreamState.Loading }
            commands { +StreamCommand.LoadData(event.streamDestination) }
        }

        is StreamEvent.Ui.ReloadData -> {
            state { StreamState.Loading }
            commands { +StreamCommand.LoadData(event.streamDestination) }
        }

        is StreamEvent.Ui.QueryChanged -> {
            state { StreamState.Loading }
            commands {
                +StreamCommand.ProcessSearch(
                    query = event.newQuery,
                    streamDestination = event.streamDestination
                )
            }
        }

        is StreamEvent.Ui.OnStreamClick -> state {
            when (event.streamDestination) {
                StreamDestination.ALL -> {
                    allStreamsListCached.value = handleStreamClick(
                        stream = event.stream,
                        listWhereHandleStreamClick = allStreamsListCached.value
                    )
                    StreamState.Content(
                        allStreamsList = allStreamsListCached.value,
                        subscribedStreamsList = subscribedStreamsListCached.value
                    )
                }

                StreamDestination.SUBSCRIBED -> {
                    subscribedStreamsListCached.value = handleStreamClick(
                        stream = event.stream,
                        listWhereHandleStreamClick = subscribedStreamsListCached.value
                    )
                    StreamState.Content(
                        allStreamsList = allStreamsListCached.value,
                        subscribedStreamsList = subscribedStreamsListCached.value
                    )
                }
            }
        }

        is StreamEvent.Ui.OnTopicClick -> effects {
            +StreamEffect.OnTopicClick(
                topicName = event.topic.name,
                streamName = findStreamNameByItsTopicIdInListSource(
                    listWhereToFind = when (event.streamDestination) {
                        StreamDestination.ALL -> {
                            allStreamsListCached.value
                        }
                        StreamDestination.SUBSCRIBED -> {
                            subscribedStreamsListCached.value
                        }
                    },
                    topicId = event.topic.id
                )
            )
        }
    }


    private fun handleStreamClick(
        listWhereHandleStreamClick: List<DelegateItem>,
        stream: StreamDelegateItem
    ): List<DelegateItem> {
        val streamModel = stream.value
        if (streamModel.isExpanded) {
            val newListWithoutTopics = deleteTopics(
                listWhereDeleteTopics = listWhereHandleStreamClick,
                stream = stream,
                streamModel = streamModel
            )
            return newListWithoutTopics
        } else {
            val newListWithTopics = addTopics(
                listWhereAddTopics = listWhereHandleStreamClick,
                stream = stream,
                streamModel = streamModel
            )
            return newListWithTopics
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
                    val topicDelegateItem =
                        TopicDelegateItem(id = generateRandomId(), value = topic)
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

    private fun findStreamNameByItsTopicIdInListSource(
        listWhereToFind: List<DelegateItem>,
        topicId: Int
    ): String {
        var topicIndex: Int? = null
        listWhereToFind.forEachIndexed { index, delegateItem ->
            val content = delegateItem.content()
            if (content is Topic && content.id == topicId) {
                topicIndex = index
            }
        }
        if (topicIndex != null) {
            for (i in topicIndex!! downTo 0) {
                if (listWhereToFind[i] is StreamDelegateItem) {
                    return (listWhereToFind[i] as StreamDelegateItem).value.name
                }
            }
        }
        throw IllegalStateException("Invalid stream")
    }
}