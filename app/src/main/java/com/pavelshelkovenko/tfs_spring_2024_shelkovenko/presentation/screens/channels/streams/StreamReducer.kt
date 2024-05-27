package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.channels.streams

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Stream
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.StreamDestination
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.SubscriptionStatus
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

        is StreamEvent.Internal.DataLoaded -> {
            state {
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
        }

        is StreamEvent.Internal.Error -> {
            if (state is StreamState.Content) {
                effects { +StreamEffect.MinorError(errorMessageId = event.errorMessageId) }
            } else {
                state { StreamState.Error(errorMessageId = event.errorMessageId) }
                effects { +StreamEffect.MinorError(errorMessageId = event.errorMessageId) }
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
            createNewStream(
                streamName = event.streamName,
                newStreamId = event.newStreamId,
                streamDestination = event.streamDestination
            )
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

        is StreamEvent.Internal.SubscriptionChangedSuccessfully -> {
            handleUpdatingSubscriptionStatus(
                streamId = event.streamId,
                newSubscriptionStatus = event.newSubscriptionStatus,
                streamDestination = event.streamDestination,
                onUpdateSuccessfully = {
                    state {
                        StreamState.Content(
                            allStreamsList = allStreamsListCached.value,
                            subscribedStreamsList = subscribedStreamsListCached.value
                        )
                    }
                }
            )
        }
    }

    override fun Result.ui(event: StreamEvent.Ui) = when (event) {

        is StreamEvent.Ui.StartProcess -> {
            commands { +StreamCommand.LoadDataFromCache(event.streamDestination) }
        }

        is StreamEvent.Ui.ReloadData -> {
            state { StreamState.Loading }
            if (event.currentQuery.isEmpty()) {
                commands { +StreamCommand.LoadDataFromCache(event.streamDestination) }
            } else {
                commands {
                    +StreamCommand.ProcessSearch(
                        query = event.currentQuery,
                        streamDestination = event.streamDestination
                    )
                }
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
            commands {
                +StreamCommand.CreateStream(
                    streamName = event.streamName,
                    streamDestination = event.streamDestination
                )
            }
        }

        is StreamEvent.Ui.ChangeSubscriptionStatus -> {
            when (event.stream.value.subscriptionStatus) {
                SubscriptionStatus.SUBSCRIBED -> {
                    commands {
                        +StreamCommand.UnsubscribedFromStream(
                            streamId = event.stream.id,
                            streamName = event.stream.value.name,
                            streamDestination = event.streamDestination
                        )
                    }
                }

                SubscriptionStatus.UNSUBSCRIBED -> {
                    commands {
                        +StreamCommand.SubscribedToStream(
                            streamId = event.stream.id,
                            streamName = event.stream.value.name,
                            streamDestination = event.streamDestination
                        )
                    }
                }
            }

        }
    }

    private fun createNewStream(
        streamName: String,
        newStreamId: Int,
        streamDestination: StreamDestination,
    ) {
        val newStream = StreamDelegateItem(
            id = newStreamId,
            value = Stream(
                id = newStreamId,
                name = streamName,
                subscriptionStatus = SubscriptionStatus.SUBSCRIBED
            )
        )
        when(streamDestination) {
            StreamDestination.ALL -> {
                allStreamsListCached.value = allStreamsListCached.value.toMutableList().apply {
                    this.add(newStream)
                }
            }
            StreamDestination.SUBSCRIBED -> {
                subscribedStreamsListCached.value = subscribedStreamsListCached.value.toMutableList().apply {
                    this.add(newStream)
                }
            }
        }
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

    private fun changeStreamSubscriptionInAllStreamsList(
        streamId: Int,
        newSubscriptionStatus: SubscriptionStatus
    ) {
        val oldStreamDelegateItem = try {
            allStreamsListCached.value.first { it.id() == streamId }
        } catch (e: Exception) {
            return
        }
        val oldStreamDelegateItemIndex =
            allStreamsListCached.value.indexOf(oldStreamDelegateItem)
        if (oldStreamDelegateItemIndex == -1) {
            return
        }
        val newStreamDelegateItem = StreamDelegateItem(
            id = streamId,
            value = (oldStreamDelegateItem.content() as Stream).copy(
                subscriptionStatus = newSubscriptionStatus
            )
        )
        allStreamsListCached.value =
            allStreamsListCached.value.toMutableList().apply {
                this[oldStreamDelegateItemIndex] = newStreamDelegateItem
            }
    }

    private fun handleUpdatingSubscriptionStatus(
        streamId: Int,
        newSubscriptionStatus: SubscriptionStatus,
        streamDestination: StreamDestination,
        onUpdateSuccessfully: () -> Unit,
    ) {
        when (streamDestination) {
            StreamDestination.ALL -> {
                when (newSubscriptionStatus) {
                    SubscriptionStatus.SUBSCRIBED -> {
                        changeStreamSubscriptionInAllStreamsList(
                            streamId = streamId,
                            newSubscriptionStatus = SubscriptionStatus.SUBSCRIBED
                        )
                        onUpdateSuccessfully()
                    }

                    SubscriptionStatus.UNSUBSCRIBED -> {
                        changeStreamSubscriptionInAllStreamsList(
                            streamId = streamId,
                            newSubscriptionStatus = SubscriptionStatus.UNSUBSCRIBED
                        )
                        onUpdateSuccessfully()
                    }
                }
            }

            StreamDestination.SUBSCRIBED -> {
                when (newSubscriptionStatus) {
                    SubscriptionStatus.SUBSCRIBED -> return
                    SubscriptionStatus.UNSUBSCRIBED -> {
                        val oldStreamDelegateItem = try {
                            subscribedStreamsListCached.value.first { it.id() == streamId }
                        } catch (e: Exception) {
                            return
                        }
                        val oldStreamDelegateItemIndex =
                            subscribedStreamsListCached.value.indexOf(oldStreamDelegateItem)
                        if (oldStreamDelegateItemIndex == -1) {
                            return
                        }
                        subscribedStreamsListCached.value =
                            subscribedStreamsListCached.value.toMutableList().apply {
                                this.remove(oldStreamDelegateItem)
                            }
                        onUpdateSuccessfully()
                    }
                }
            }
        }
    }
}