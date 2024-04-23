package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.streams

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.StreamRepository
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.StreamDestination
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.runCatchingNonCancellation
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
                }.onFailure { error ->
                    emit(StreamEvent.Internal.Error(throwable = error))
                }
            }

            is StreamCommand.LoadData -> flow {
                runCatchingNonCancellation {
                    when (command.streamDestination) {
                        StreamDestination.ALL -> {
                            repository.getAllStreams()
                        }
                        StreamDestination.SUBSCRIBED -> {
                            repository.getSubscribedStreams()
                        }
                    }
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
        }
    }


}