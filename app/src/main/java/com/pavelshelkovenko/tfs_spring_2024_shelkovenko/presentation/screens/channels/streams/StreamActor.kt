package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.channels.streams

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.SubscriptionStatus
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.repository.StreamRepository
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.runCatchingNonCancellation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.core.store.Actor

class StreamActor(
    private val repository: StreamRepository
) : Actor<StreamCommand, StreamEvent>() {
    override fun execute(command: StreamCommand): Flow<StreamEvent> {
        return when (command) {
            is StreamCommand.ProcessSearch -> flow {
                runCatchingNonCancellation {
                    repository.searchStreams(command.query, command.streamDestination)
                }.onSuccess { streams ->
                    emit(
                        StreamEvent.Internal.DataLoaded(
                            streams = streams,
                            streamDestination = command.streamDestination
                        )
                    )
                }.onFailure {
                    emit(StreamEvent.Internal.MinorError(errorMessageId = R.string.search_error))
                    runCatchingNonCancellation {
                        repository.searchStreamsInCache(
                            query = command.query,
                            streamDestination = command.streamDestination
                        )
                    }.onSuccess { streams ->
                        if (streams.isNotEmpty()) {
                            emit(
                                StreamEvent.Internal.DataLoaded(
                                    streams = streams,
                                    streamDestination = command.streamDestination
                                )
                            )
                        } else {
                            emit(StreamEvent.Internal.SearchError(errorMessageId = R.string.search_error))
                        }
                    }
                }
            }

            is StreamCommand.LoadDataFromNetwork -> flow {
                runCatchingNonCancellation {
                    repository.getStreamsByDestinationFromNetwork(command.streamDestination)
                }.onSuccess { streams ->
                    emit(
                        StreamEvent.Internal.DataLoaded(
                            streams = streams,
                            streamDestination = command.streamDestination
                        )
                    )
                }.onFailure {
                    emit(StreamEvent.Internal.Error(errorMessageId = R.string.load_streams_error))
                }
            }

            is StreamCommand.LoadDataFromCache -> flow {
                runCatchingNonCancellation {
                    repository.getStreamsByDestinationFromCache(command.streamDestination)
                }.onSuccess { streams ->
                    emit(
                        StreamEvent.Internal.DataLoadedFromCache(
                            streams = streams,
                            streamDestination = command.streamDestination
                        )
                    )
                }.onFailure {
                    emit(StreamEvent.Internal.ErrorLoadingFromCache(command.streamDestination))
                }
            }

            is StreamCommand.LoadTopicsForStream -> flow {
                runCatchingNonCancellation {
                    repository.getTopicsForStreamById(streamId = command.stream.id)
                }.onSuccess { topics ->
                    emit(
                        StreamEvent.Internal.TopicsLoaded(
                            topics = topics,
                            stream = command.stream,
                            streamDestination = command.streamDestination
                        )
                    )
                }.onFailure {
                    emit(StreamEvent.Internal.MinorError(errorMessageId = R.string.load_topics_error))
                }
            }

            is StreamCommand.CreateStream -> flow {
                runCatchingNonCancellation {
                    repository.createStream(command.streamName)
                }.onSuccess { newStreamId ->
                    emit(
                        StreamEvent.Internal.StreamCreatedSuccessfully(
                            streamName = command.streamName,
                            newStreamId = newStreamId,
                            streamDestination = command.streamDestination
                        )
                    )
                }.onFailure {
                    emit(StreamEvent.Internal.MinorError(errorMessageId = R.string.create_stream_error))
                }
            }

            is StreamCommand.SubscribedToStream -> flow {
                runCatchingNonCancellation {
                    repository.subscribeToStream(
                        streamName = command.streamName,
                        streamId = command.streamId
                    )
                }.onSuccess {
                    emit(
                        StreamEvent.Internal.SubscriptionChangedSuccessfully(
                            streamId = command.streamId,
                            newSubscriptionStatus = SubscriptionStatus.SUBSCRIBED,
                            streamDestination = command.streamDestination
                        )
                    )
                }.onFailure {
                    emit(StreamEvent.Internal.MinorError(errorMessageId = R.string.subscribe_to_stream_error))
                }
            }

            is StreamCommand.UnsubscribedFromStream -> flow {
                runCatchingNonCancellation {
                    repository.unsubscribeFromStream(
                        streamName = command.streamName,
                        streamId = command.streamId
                    )
                }.onSuccess {
                    emit(
                        StreamEvent.Internal.SubscriptionChangedSuccessfully(
                            streamId = command.streamId,
                            newSubscriptionStatus = SubscriptionStatus.UNSUBSCRIBED,
                            streamDestination = command.streamDestination
                        )
                    )
                }.onFailure {
                    emit(StreamEvent.Internal.MinorError(errorMessageId = R.string.unsubscribe_from_stream_error))
                }
            }
        }
    }


}