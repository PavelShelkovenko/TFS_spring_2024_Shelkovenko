package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.delegate_adapter.DelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.StreamRepository
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Stream
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.StreamDestination
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Topic
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.generateRandomId
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.streams.StreamDelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.topics.TopicDelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.runCatchingNonCancellation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update


class ChannelsViewModel(
    private val repository: StreamRepository
) : ViewModel() {

    private val _allStreamsScreenState =
        MutableStateFlow<ChannelsScreenState>(ChannelsScreenState.Initial)
    val allStreamsScreenState = _allStreamsScreenState.asStateFlow()

    private val _subscribedStreamsScreenState =
        MutableStateFlow<ChannelsScreenState>(ChannelsScreenState.Initial)
    val subscribedScreenState = _subscribedStreamsScreenState.asStateFlow()

    private val allStreamsList = MutableStateFlow<List<DelegateItem>>(emptyList())
    private val subscribedStreamsList = MutableStateFlow<List<DelegateItem>>(emptyList())

    val searchQueryFlow = MutableStateFlow("")

    val streamDestination = MutableStateFlow(StreamDestination.SUBSCRIBED)

    init {
        collectSearchQuery()
    }

    private fun collectSearchQuery() {
        searchQueryFlow
            .distinctUntilChanged { old, new -> old.contentEquals(new) }
            .debounce(1000L)
            .drop(1)
            .onEach { processSearch() }
            .flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)
    }

    suspend fun refreshScreenState() {
        doActionOnStreamDestination(
            actionOnAllStreams = {
                processSearch()
            },
            actionOnSubscribedStreams = {
                processSearch()
            }
        )
    }

    suspend fun processSearch() {
        doActionOnStreamDestination(
            actionOnAllStreams = {
                _allStreamsScreenState.value = ChannelsScreenState.Loading
            },
            actionOnSubscribedStreams = {
                _subscribedStreamsScreenState.value = ChannelsScreenState.Loading
            }
        )
        runCatchingNonCancellation {
            val streamsList = repository.searchStreams(searchQueryFlow.value, streamDestination.value)
            streamsList.map { stream ->
                StreamDelegateItem(
                    id = generateRandomId(),
                    value = stream
                )
            }
        }.onSuccess {
            doActionOnStreamDestination(
                actionOnAllStreams = {
                    _allStreamsScreenState.value = ChannelsScreenState.Content(it)
                },
                actionOnSubscribedStreams = {
                    _subscribedStreamsScreenState.value = ChannelsScreenState.Content(it)
                }
            )
        }.onFailure { error ->
            doActionOnStreamDestination(
                actionOnAllStreams = {
                    _allStreamsScreenState.value =
                        ChannelsScreenState.Error(error.message.toString())
                },
                actionOnSubscribedStreams = {
                    _subscribedStreamsScreenState.value =
                        ChannelsScreenState.Error(error.message.toString())
                }
            )
        }
    }



    suspend fun setupStubData() {
        doActionOnStreamDestination(
            actionOnSubscribedStreams = {
                _subscribedStreamsScreenState.value = ChannelsScreenState.Loading
            },
            actionOnAllStreams = {
                _allStreamsScreenState.value = ChannelsScreenState.Loading
            }
        )
        runCatchingNonCancellation {
            when (streamDestination.value) {
                StreamDestination.AllSTREAMS -> {
                    val streamsList = repository.getAllStreams()
                    val delegateList = streamsList.map { stream ->
                        StreamDelegateItem(
                            id = generateRandomId(),
                            value = stream
                        )
                    }
                    allStreamsList.value = delegateList
                }
                StreamDestination.SUBSCRIBED -> {
                    val streamsList = repository.getSubscribedStreams()
                    val delegateList = streamsList.map { stream ->
                        StreamDelegateItem(
                            id = generateRandomId(),
                            value = stream
                        )
                    }
                    subscribedStreamsList.value = delegateList
                }
            }
        }.onSuccess {
            doActionOnStreamDestination(
                actionOnSubscribedStreams = {
                    _subscribedStreamsScreenState.value =
                        ChannelsScreenState.Content(streamsList = subscribedStreamsList.value)
                },
                actionOnAllStreams = {
                    _allStreamsScreenState.value =
                        ChannelsScreenState.Content(streamsList = allStreamsList.value)
                }
            )
        }.onFailure { error ->
            doActionOnStreamDestination(
                actionOnSubscribedStreams = {
                    _subscribedStreamsScreenState.value =
                        ChannelsScreenState.Error(error.message.toString())
                },
                actionOnAllStreams = {
                    _allStreamsScreenState.value =
                        ChannelsScreenState.Error(error.message.toString())
                }
            )
        }
    }

    fun onStreamClick(stream: StreamDelegateItem) {
        doActionOnStreamDestination(
            actionOnAllStreams = {
                handleStreamClick(
                    screenStateForUpdate = _allStreamsScreenState,
                    stream = stream
                )
            },
            actionOnSubscribedStreams = {
                handleStreamClick(
                    screenStateForUpdate = _subscribedStreamsScreenState,
                    stream = stream
                )
            }
        )
    }


    fun findStreamByItsTopicId(topicId: Int): Stream {
        var result: Stream? = null
        doActionOnStreamDestination(
            actionOnAllStreams = {
                result = findStreamByItsTopicIdInListSource(
                    (_allStreamsScreenState.value as ChannelsScreenState.Content).streamsList,
                    topicId
                )
            },
            actionOnSubscribedStreams = {
                result = findStreamByItsTopicIdInListSource(
                    (_subscribedStreamsScreenState.value as ChannelsScreenState.Content).streamsList,
                    topicId
                )
            }
        )
        return result ?: throw IllegalStateException("Invalid stream")
    }

    private fun handleStreamClick(
        screenStateForUpdate: MutableStateFlow<ChannelsScreenState>,
        stream: StreamDelegateItem,
    ) {
        val streamModel = stream.value
        if (streamModel.isExpanded) {
            val newListWithoutTopics = deleteTopics(
                listWhereDeleteTopics = (screenStateForUpdate.value as ChannelsScreenState.Content).streamsList,
                stream = stream,
                streamModel = streamModel
            )
            screenStateForUpdate.update {
                (it as ChannelsScreenState.Content).copy(
                    streamsList = newListWithoutTopics
                )
            }
            doActionOnStreamDestination(
                actionOnAllStreams = { allStreamsList.value = newListWithoutTopics },
                actionOnSubscribedStreams = { subscribedStreamsList.value = newListWithoutTopics }
            )
        } else {
            val newListWithTopics = addTopics(
                listWhereAddTopics = (screenStateForUpdate.value as ChannelsScreenState.Content).streamsList,
                stream = stream,
                streamModel = streamModel
            )
            screenStateForUpdate.update {
                (it as ChannelsScreenState.Content).copy(
                    streamsList = newListWithTopics
                )
            }
            doActionOnStreamDestination(
                actionOnAllStreams = { allStreamsList.value = newListWithTopics },
                actionOnSubscribedStreams = { subscribedStreamsList.value = newListWithTopics }
            )
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
                    val topicDelegateItem = TopicDelegateItem(id = generateRandomId(), value = topic)
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

    private fun findStreamByItsTopicIdInListSource(
        listWhereToFind: List<DelegateItem>,
        topicId: Int
    ): Stream? {
        val result: Stream? = null
        var topicIndex: Int? = null
        listWhereToFind.forEachIndexed { index, delegateItem ->
            try {
                if ((delegateItem.content() as Topic).id == topicId) {
                    topicIndex = index
                }
            } catch (_: Exception) {}
        }
        if (topicIndex != null) {
            for (i in topicIndex!! downTo 0) {
                if (listWhereToFind[i] is StreamDelegateItem) {
                    return (listWhereToFind[i] as StreamDelegateItem).value
                }
            }
        }
        return result
    }

    private inline fun doActionOnStreamDestination(
        actionOnAllStreams: () -> Unit,
        actionOnSubscribedStreams: () -> Unit,
    ) {
        when (streamDestination.value) {
            StreamDestination.AllSTREAMS -> {
                actionOnAllStreams.invoke()
            }
            StreamDestination.SUBSCRIBED -> {
                actionOnSubscribedStreams.invoke()
            }
        }
    }
}