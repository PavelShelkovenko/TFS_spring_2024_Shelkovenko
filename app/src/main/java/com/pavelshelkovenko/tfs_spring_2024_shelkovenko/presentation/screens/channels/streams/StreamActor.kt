package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.channels.streams

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
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
                    emit(StreamEvent.Internal.MinorError(errorMessageId = R.string.load_streams_error))
                }
            }

            is StreamCommand.LoadData -> flow {
                runCatchingNonCancellation {
                    repository.getStreamsByDestination(command.streamDestination)
                }.onSuccess { streams ->
                    emit(
                        StreamEvent.Internal.DataLoaded(
                            streams = streams,
                            streamDestination = command.streamDestination
                        )
                    )
                }.onFailure { error ->
                    emit(StreamEvent.Internal.Error(throwable = error))
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
                    emit(StreamEvent.Internal.MinorError(errorMessageId = R.string.load_streams_error))
                }
            }
        }
    }


}