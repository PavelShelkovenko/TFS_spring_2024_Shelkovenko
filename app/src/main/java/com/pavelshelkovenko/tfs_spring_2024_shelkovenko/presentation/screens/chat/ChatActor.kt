package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat

import android.util.Log
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.RegistrationForEventsData
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.repository.ChatRepository
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.runCatchingNonCancellation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.core.store.Actor

class ChatActor(
    private val repository: ChatRepository
) : Actor<ChatCommand, ChatEvent>() {
    override fun execute(command: ChatCommand): Flow<ChatEvent> {
        return when (command) {
            is ChatCommand.LoadMessagesFromNetwork -> flow {
                runCatchingNonCancellation {
                    repository.getMessagesFromNetwork(
                        streamName = command.streamName,
                        topicName = command.topicName,
                        anchor = command.anchor,
                        numBefore = command.numBefore,
                        numAfter = command.numAfter
                    )
                }.onSuccess { messages ->
                    emit(ChatEvent.Internal.LoadMessagesFromNetwork(messages = messages))
                }.onFailure {
                    emit(ChatEvent.Internal.MinorError(errorMessageId = R.string.load_messages_error))
                }
            }

            is ChatCommand.LoadMessagesFromCache -> flow {
                runCatchingNonCancellation {
                    repository.getMessagesFromCache(
                        streamName = command.streamName,
                        topicName = command.topicName,
                    )
                }.onSuccess { messages ->
                    emit(ChatEvent.Internal.LoadMessagesFromCache(messages = messages))
                }.onFailure { error ->
                    emit(ChatEvent.Internal.Error(throwable = error))
                }
            }

            is ChatCommand.LoadPagingNewerMessages -> flow {
                runCatchingNonCancellation {
                    repository.getPagingMessages(
                        streamName = command.streamName,
                        topicName = command.topicName,
                        anchor = command.anchor,
                        numBefore = command.numBefore,
                        numAfter = command.numAfter
                    )
                }.onSuccess { messages ->
                    emit(ChatEvent.Internal.LoadPagingNewerMessages(messages = messages))
                }.onFailure {
                    emit(ChatEvent.Internal.MinorError(errorMessageId = R.string.load_messages_error))
                    delay(10000)
                }
                emit(ChatEvent.Internal.LoadingPagingDataFinished)
            }

            is ChatCommand.LoadPagingOlderMessages -> flow {
                runCatchingNonCancellation {
                    repository.getPagingMessages(
                        streamName = command.streamName,
                        topicName = command.topicName,
                        anchor = command.anchor,
                        numBefore = command.numBefore,
                        numAfter = command.numAfter
                    )
                }.onSuccess { messages ->
                    emit(ChatEvent.Internal.LoadPagingOlderMessages(messages = messages))
                }.onFailure {
                    emit(ChatEvent.Internal.MinorError(errorMessageId = R.string.load_messages_error))
                    /*
                    Сделано для того, чтобы если пользователь пытается загрузить новые или старые
                    сообщения при отсутствии интернета, то ему приходил Toast об отсутвии интернета
                    только раз в 10 сек(при условии что он попытался загрузить), а не постоянно
                     */
                    delay(10000)
                }
                emit(ChatEvent.Internal.LoadingPagingDataFinished)
            }

            is ChatCommand.RemoveReaction -> flow {
                runCatchingNonCancellation {
                    repository.removeReaction(
                        messageId = command.messageId,
                        emojiName = command.emojiName,
                        emojiCode = command.emojiCode
                    )
                }.onFailure {
                    emit(ChatEvent.Internal.MinorError(errorMessageId = R.string.remove_reaction_error))
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
                    emit(ChatEvent.Internal.MinorError(errorMessageId = R.string.send_message_error))
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
                    emit(ChatEvent.Internal.MinorError(errorMessageId = R.string.send_reaction_error))
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
                    // Небольшая задержка для того, чтобы не обработать один и тот же эвент два раза
                    delay(100)
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
                    // Небольшая задержка для того, чтобы не обработать один и тот же эвент два раза
                    delay(100)
                    emit(ChatEvent.Internal.GetReactionLongPollingData)
                }.onFailure { error ->
                    Log.d("ChatActor", "${error.message}")
                    emit(ChatEvent.Internal.GetReactionLongPollingData)
                }

            }

            is ChatCommand.SaveMessagesInCache -> flow {
                runCatchingNonCancellation {
                    repository.saveMessagesInCache(
                        streamName = command.streamName,
                        topicName = command.topicName,
                        messages = command.messages
                    )
                }.onSuccess {
                    emit(ChatEvent.Internal.CachedMessagesSaved)
                }.onFailure { error ->
                    Log.d("ChatActor", "${error.message}")
                }
            }
        }
    }
}