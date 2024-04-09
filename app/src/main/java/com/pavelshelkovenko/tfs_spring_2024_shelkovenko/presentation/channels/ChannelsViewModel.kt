package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.delegate_adapter.DelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.homework_5.GetStubStreamsUseCase
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.streams.Stream
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.streams.StreamDelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.streams.StreamDestination
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.topics.Topic
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
    private val stubStreamsUseCase: GetStubStreamsUseCase
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

    val streamDestination = MutableStateFlow(StreamDestination.Subscribed)

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
            stubStreamsUseCase.search(searchQueryFlow.value.trim(), streamDestination.value)
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
                StreamDestination.AllStreams -> {
                    allStreamsList.value = stubStreamsUseCase.invoke(StreamDestination.AllStreams)
                }
                StreamDestination.Subscribed -> {
                    subscribedStreamsList.value =  stubStreamsUseCase.invoke(StreamDestination.Subscribed)
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


    fun findStreamByItsTopic(topic: Topic): Stream {
        var result: Stream? = null
        doActionOnStreamDestination(
            actionOnAllStreams = {
                result = findStreamByItsTopicInListSource(
                    (_allStreamsScreenState.value as ChannelsScreenState.Content).streamsList,
                    topic
                )
            },
            actionOnSubscribedStreams = {
                result = findStreamByItsTopicInListSource(
                    (_subscribedStreamsScreenState.value as ChannelsScreenState.Content).streamsList,
                    topic
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

    private fun findStreamByItsTopicInListSource(
        listWhereToFind: List<DelegateItem>,
        topic: Topic
    ): Stream? {
        var result: Stream? = null
        listWhereToFind.forEach { delegateItem ->
            try {
                val resultTopic =
                    ((delegateItem as StreamDelegateItem).value.topicsList.find {
                        it.id == topic.id
                    })
                if (resultTopic != null) {
                    result = delegateItem.value
                }
            } catch (_: Exception) {
            }
        }
        return result
    }

    private inline fun doActionOnStreamDestination(
        actionOnAllStreams: () -> Unit,
        actionOnSubscribedStreams: () -> Unit,
    ) {
        when (streamDestination.value) {
            StreamDestination.AllStreams -> {
                actionOnAllStreams.invoke()
            }
            StreamDestination.Subscribed -> {
                actionOnSubscribedStreams.invoke()
            }
        }
    }
}