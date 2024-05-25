package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.channels.streams

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Stream
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.StreamDestination
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Topic
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.base.delegate_adapter.DelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.channels.streams.adapter.StreamDelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.channels.topics.TopicDelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.NoAction
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.generateRandomId
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.toDelegateList
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

        is StreamEvent.Internal.DataLoadedFromNetwork -> {
            state {
                when (event.streamDestination) {
                    StreamDestination.ALL -> {
                        val expandedList =
                            getLoadedExpandedDelegateList(allStreamsListCached.value, event.streams)
                        allStreamsListCached.value = expandedList
                        StreamState.Content(
                            allStreamsList = expandedList,
                            subscribedStreamsList = subscribedStreamsListCached.value
                        )
                    }

                    StreamDestination.SUBSCRIBED -> {
                        val expandedList = getLoadedExpandedDelegateList(
                            subscribedStreamsListCached.value,
                            event.streams
                        )
                        subscribedStreamsListCached.value = expandedList
                        StreamState.Content(
                            allStreamsList = allStreamsListCached.value,
                            subscribedStreamsList = expandedList
                        )
                    }
                }
            }
        }

        is StreamEvent.Internal.Error -> {
            if (state is StreamState.Content) {
                effects { +StreamEffect.MinorError(errorMessageId = event.errorMessageId) }
            } else {
                state { StreamState.Error(errorMessageId = event.errorMessageId) }
            }

        }

        is StreamEvent.Internal.MinorError -> effects {
            +StreamEffect.MinorError(event.errorMessageId)
        }

        is StreamEvent.Internal.DataLoadedFromCache -> {
            if (event.streams.isEmpty()) {
                state { StreamState.Loading }
            } else {
                when (event.streamDestination) {
                    StreamDestination.ALL -> {
                        allStreamsListCached.value = event.streams.toDelegateList()
                        state {
                            StreamState.Content(
                                allStreamsList = allStreamsListCached.value,
                                subscribedStreamsList = subscribedStreamsListCached.value
                            )
                        }
                    }

                    StreamDestination.SUBSCRIBED -> {
                        subscribedStreamsListCached.value = event.streams.toDelegateList()
                        state {
                            StreamState.Content(
                                allStreamsList = allStreamsListCached.value,
                                subscribedStreamsList = subscribedStreamsListCached.value
                            )
                        }
                    }
                }
            }
            commands { +StreamCommand.LoadDataFromNetwork(event.streamDestination) }
        }

        is StreamEvent.Internal.TopicsLoaded -> {
            state {
                updateStreamsStateAfterStreamClick(
                    stream = event.stream,
                    topics = event.topics,
                    streamDestination = event.streamDestination
                )
            }
        }

        is StreamEvent.Internal.StreamCreatedSuccessfully -> {
            createNewStream(streamName = event.streamName, newStreamId = event.newStreamId)
            state {
                StreamState.Content(
                    allStreamsList = allStreamsListCached.value,
                    subscribedStreamsList = subscribedStreamsListCached.value
                )
            }
        }

        is StreamEvent.Internal.ErrorLoadingFromCache -> {
            commands { +StreamCommand.LoadDataFromNetwork(event.streamDestination) }
        }

        is StreamEvent.Internal.SearchError -> {
            state { StreamState.Error(errorMessageId = event.errorMessageId) }
        }
    }

    override fun Result.ui(event: StreamEvent.Ui) = when (event) {

        is StreamEvent.Ui.StartProcess -> {
            commands { +StreamCommand.LoadDataFromCache(event.streamDestination) }
        }

        is StreamEvent.Ui.ReloadData -> {
            state { StreamState.Loading }
            commands {
                +StreamCommand.ProcessSearch(
                    query = event.currentQuery,
                    streamDestination = event.streamDestination
                )
            }
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

        is StreamEvent.Ui.OnStreamClick -> {
            if (event.stream.value.isExpanded) {
                state {
                    updateStreamsStateAfterStreamClick(
                        stream = event.stream,
                        streamDestination = event.streamDestination
                    )
                }
            } else {
                commands {
                    +StreamCommand.LoadTopicsForStream(
                        stream = event.stream,
                        streamDestination = event.streamDestination
                    )
                }
            }
        }

        is StreamEvent.Ui.OnTopicClick -> {
            var errorFlag = false
            val streamId = try {
                findStreamIdByItsTopicId(
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
            } catch (_: Exception) {
                errorFlag = true
            }
            val streamName = try {
                findStreamNameByItsTopicId(
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
            } catch (_: Exception) {
                errorFlag = true
            }
            if (errorFlag) {
                NoAction
            } else {
                effects {
                    +StreamEffect.NavigateToChat(
                        topicName = event.topic.name,
                        streamName = streamName as String,
                        streamId = streamId as Int
                    )
                }
            }
        }

        is StreamEvent.Ui.CreateStream -> {
            commands { +StreamCommand.CreateStream(event.streamName) }
        }
    }

    private fun createNewStream(streamName: String, newStreamId: Int) {
        val newStream = StreamDelegateItem(
            id = newStreamId,
            value = Stream(
                id = newStreamId,
                name = streamName,
            )
        )
        val newSubscribedStreamsList = subscribedStreamsListCached.value.toMutableList()
        newSubscribedStreamsList.add(newStream)
        subscribedStreamsListCached.value = newSubscribedStreamsList
    }

    private fun updateStreamsStateAfterStreamClick(
        stream: StreamDelegateItem,
        topics: List<Topic> = stream.value.topicsList,
        streamDestination: StreamDestination
    ): StreamState {
        return when (streamDestination) {
            StreamDestination.ALL -> {
                allStreamsListCached.value = handleStreamClick(
                    stream = stream,
                    topics = topics,
                    listWhereHandleStreamClick = allStreamsListCached.value
                )
                StreamState.Content(
                    allStreamsList = allStreamsListCached.value,
                    subscribedStreamsList = subscribedStreamsListCached.value
                )
            }

            StreamDestination.SUBSCRIBED -> {
                subscribedStreamsListCached.value = handleStreamClick(
                    stream = stream,
                    topics = topics,
                    listWhereHandleStreamClick = subscribedStreamsListCached.value
                )
                StreamState.Content(
                    allStreamsList = allStreamsListCached.value,
                    subscribedStreamsList = subscribedStreamsListCached.value
                )
            }
        }
    }

    private fun getLoadedExpandedDelegateList(
        streamList: List<DelegateItem>,
        streamListFromNetwork: List<Stream>,
    ): List<DelegateItem> {
        val expandedStreamsId = streamList
            .filterIsInstance<StreamDelegateItem>()
            .filter { it.value.isExpanded }
            .map { it.id }
        val expandedList = mutableListOf<DelegateItem>()
        streamListFromNetwork.forEach { stream ->
            if (stream.id in expandedStreamsId) {
                expandedList.add(
                    StreamDelegateItem(
                        id = stream.id,
                        value = stream.copy(isExpanded = true)
                    )
                )
                for (topic in stream.topicsList) {
                    val topicDelegateItem =
                        TopicDelegateItem(id = generateRandomId(), value = topic)
                    expandedList.add(topicDelegateItem)
                }
            } else {
                expandedList.add(StreamDelegateItem(id = stream.id, value = stream))
            }
        }
        return expandedList
    }

    private fun handleStreamClick(
        listWhereHandleStreamClick: List<DelegateItem>,
        stream: StreamDelegateItem,
        topics: List<Topic>,
    ): List<DelegateItem> {
        val streamModel = stream.value
        val newListWithoutTopics = if (streamModel.isExpanded) {
            deleteTopics(
                listWhereDeleteTopics = listWhereHandleStreamClick,
                stream = stream,
                topics = topics,
                streamModel = streamModel
            )
        } else {
            addTopics(
                listWhereAddTopics = listWhereHandleStreamClick,
                stream = stream,
                topics = topics,
                streamModel = streamModel
            )
        }
        return newListWithoutTopics
    }


    private fun addTopics(
        listWhereAddTopics: List<DelegateItem>,
        stream: StreamDelegateItem,
        topics: List<Topic>,
        streamModel: Stream
    ): List<DelegateItem> {
        val newList = mutableListOf<DelegateItem>()
        for (delegateItem in listWhereAddTopics) {
            if (stream.id != delegateItem.id()) {
                newList.add(delegateItem)
            } else {
                val newStreamDelegateItem = StreamDelegateItem(
                    id = stream.id,
                    value = streamModel.copy(isExpanded = true, topicsList = topics)
                )
                newList.add(newStreamDelegateItem)
                for (topic in topics) {
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
        topics: List<Topic>,
        streamModel: Stream
    ): List<DelegateItem> {
        val newList = listWhereDeleteTopics.toMutableList()
        val streamIndex = newList.indexOf(stream)
        val lastTopicIndex = streamIndex + topics.size
        val newStreamDelegateItem = StreamDelegateItem(
            id = stream.id,
            value = streamModel.copy(isExpanded = false, topicsList = emptyList())
        )
        newList.removeAt(streamIndex)
        newList.add(streamIndex, newStreamDelegateItem)
        for (i in topics.indices) {
            newList.removeAt(lastTopicIndex - i)
        }
        return newList
    }

    private fun findStreamIdByItsTopicId(
        listWhereToFind: List<DelegateItem>,
        topicId: Int
    ): Int {
        var topicIndex: Int? = null
        listWhereToFind.forEachIndexed { index, delegateItem ->
            val content = delegateItem.content()
            if (content is Topic && content.id == topicId) {
                topicIndex = index
            }
        }
        topicIndex?.let { index ->
            for (i in index downTo 0) {
                if (listWhereToFind[i] is StreamDelegateItem) {
                    return (listWhereToFind[i] as StreamDelegateItem).value.id

                }
            }
        }
        throw IllegalStateException("Invalid stream id")
    }

    private fun findStreamNameByItsTopicId(
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
        topicIndex?.let { index ->
            for (i in index downTo 0) {
                if (listWhereToFind[i] is StreamDelegateItem) {
                    return (listWhereToFind[i] as StreamDelegateItem).value.name

                }
            }
        }
        throw IllegalStateException("Invalid stream name")
    }

}