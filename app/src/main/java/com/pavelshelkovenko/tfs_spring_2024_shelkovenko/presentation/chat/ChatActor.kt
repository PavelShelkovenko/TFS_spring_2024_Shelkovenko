package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat

import android.util.Log
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.ChatRepository
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.RegistrationForEventsData
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.runCatchingNonCancellation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.core.store.Actor

class ChatActor(
    private val repository: ChatRepository
) : Actor<ChatCommand, ChatEvent>() {
    override fun execute(command: ChatCommand): Flow<ChatEvent> {
        return when (command) {
            is ChatCommand.LoadInitialMessages -> flow {
                runCatchingNonCancellation {
                    repository.getMessages(
                        streamName = command.streamName,
                        topicName = command.topicName,
                        anchor = command.anchor,
                        numBefore = command.numBefore,
                        numAfter = command.numAfter
                    )
                }.onSuccess { messages ->
                    emit(ChatEvent.Internal.LoadInitialMessages(messages = messages))
                }.onFailure { error ->
                    emit(ChatEvent.Internal.Error(throwable = error))
                }
            }

            is ChatCommand.LoadPagingNewerMessages -> flow {
                runCatchingNonCancellation {
                    repository.getMessages(
                        streamName = command.streamName,
                        topicName = command.topicName,
                        anchor = command.anchor,
                        numBefore = command.numBefore,
                        numAfter = command.numAfter
                    )
                }.onSuccess { messages ->
                    emit(ChatEvent.Internal.LoadPagingNewerMessages(messages = messages))
                    emit(ChatEvent.Internal.LoadingPagingDataFinished)
                }.onFailure {
                    emit(ChatEvent.Internal.MinorError(errorMessage = LOADING_MESSAGES_ERROR_TEXT))
                    delay(10000)
                    emit(ChatEvent.Internal.LoadingPagingDataFinished)
                }
            }

            is ChatCommand.LoadPagingOlderMessages -> flow {
                runCatchingNonCancellation {
                    repository.getMessages(
                        streamName = command.streamName,
                        topicName = command.topicName,
                        anchor = command.anchor,
                        numBefore = command.numBefore,
                        numAfter = command.numAfter
                    )
                }.onSuccess { messages ->
                    emit(ChatEvent.Internal.LoadPagingOlderMessages(messages = messages))
                    emit(ChatEvent.Internal.LoadingPagingDataFinished)
                }.onFailure {
                    emit(ChatEvent.Internal.MinorError(errorMessage = LOADING_MESSAGES_ERROR_TEXT))
                    delay(10000)
                    emit(ChatEvent.Internal.LoadingPagingDataFinished)
                }
            }

            is ChatCommand.RemoveReaction -> flow {
                runCatchingNonCancellation {
                    repository.removeReaction(
                        messageId = command.messageId,
                        emojiName = command.emojiName,
                        emojiCode = command.emojiCode
                    )
                }.onFailure {
                    emit(ChatEvent.Internal.MinorError(errorMessage = REMOVING_REACTION_ERROR_TEXT))
                }
            }

            is ChatCommand.SendMessage -> flow {
                runCatchingNonCancellation {
                    repository.sendMessage(
                        streamName = command.streamName,
                        topicName = command.topicName,
                        message = command.message
                    )
                }.onFailure {
                    emit(ChatEvent.Internal.MinorError(errorMessage = SENDING_MESSAGE_ERROR_TEXT))
                }
            }

            is ChatCommand.SendReaction -> flow {
                runCatchingNonCancellation {
                    repository.sendReaction(
                        messageId = command.messageId,
                        emojiName = command.emojiName,
                        emojiCode = command.emojiCode
                    )
                }.onFailure {
                    emit(ChatEvent.Internal.MinorError(errorMessage = SENDING_REACTION_ERROR_TEXT))
                }
            }

            is ChatCommand.RegisterForChatEvents -> flow {
                runCatchingNonCancellation {
                    repository.registerForEvents(
                        streamName = command.streamName,
                        topicName = command.topicName
                    )
                }.onSuccess { registrationData ->
                    emit(
                        ChatEvent.Internal.RegistrationForChatEventsDataReceived(
                            RegistrationForEventsData(
                                messagesQueueId = registrationData.messagesQueueId,
                                messageLastEventId = registrationData.messageLastEventId,
                                reactionsQueueId = registrationData.reactionsQueueId,
                                reactionLastEventId = registrationData.reactionLastEventId
                            )
                        )
                    )
                }.onFailure { error ->
                    Log.d("ChatActor", "${error.message}")
                }
            }

            is ChatCommand.GetMessageEvents -> flow {
                runCatchingNonCancellation {
                    repository.getMessageEvents(
                        queueId = command.queueId,
                        lastEventId = command.lastEventId
                    )
                }.onSuccess { receivedData ->
                    emit(
                        ChatEvent.Internal.MessageEventsDataReceived(
                            receivedMessageEventData = receivedData
                        )
                    )
                    emit(ChatEvent.Internal.GetMessageLongPollingData)
                }.onFailure { error ->
                    Log.d("ChatActor", "${error.message}")
                    emit(ChatEvent.Internal.GetMessageLongPollingData)
                }
            }

            is ChatCommand.GetReactionEvents -> flow {
                runCatchingNonCancellation {
                    repository.getReactionEvents(
                        queueId = command.queueId,
                        lastEventId = command.lastEventId
                    )
                }.onSuccess { receivedData ->
                    emit(
                        ChatEvent.Internal.ReactionEventsDataReceived(
                            receivedReactionEventData = receivedData
                        )
                    )
                    emit(ChatEvent.Internal.GetReactionLongPollingData)
                }.onFailure { error ->
                    Log.d("ChatActor", "${error.message}")
                    emit(ChatEvent.Internal.GetReactionLongPollingData)
                }

            }
        }
    }

    companion object {
        private const val SENDING_MESSAGE_ERROR_TEXT = "Can't send message, check your internet connection"
        private const val SENDING_REACTION_ERROR_TEXT = "Can't send reaction, check your internet connection"
        private const val REMOVING_REACTION_ERROR_TEXT = "Can't remove reaction, check your internet connection"
        private const val LOADING_MESSAGES_ERROR_TEXT = "Can't load messages, check your internet connection"
    }
}