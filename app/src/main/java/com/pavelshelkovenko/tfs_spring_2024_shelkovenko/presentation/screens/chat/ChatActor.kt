package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.RegistrationForEventsData
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.repository.ChatRepository
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.usecases.GetTopicsForStreamByIdUseCase
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.runCatchingNonCancellation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.core.store.Actor

class ChatActor(
    private val repository: ChatRepository,
    private val getTopicsForStreamByIdUseCase: GetTopicsForStreamByIdUseCase
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
                    emit(ChatEvent.Internal.Error(errorMessageId = R.string.load_messages_error))
                }
            }

            is ChatCommand.LoadMessagesFromCache -> flow {
                runCatchingNonCancellation {
                    repository.getMessagesFromCache(
                        streamName = command.streamName,
                        topicName = command.topicName,
                    )
                }.onSuccess { messages ->
                    emit(
                        ChatEvent.Internal.LoadMessagesFromCache(
                            messages = messages,
                            topicName = command.topicName,
                            streamName = command.streamName
                        )
                    )
                }.onFailure {
                    emit(
                        ChatEvent.Internal.ErrorLoadingFromCache(
                            streamName = command.streamName,
                            topicName = command.topicName,
                        )
                    )
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
                    emit(ChatEvent.Internal.PagingError(errorMessageId = R.string.load_messages_error))
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
                    emit(ChatEvent.Internal.PagingError(errorMessageId = R.string.load_messages_error))
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
                }
                // TODO Разобраться, как отфильтровать одинаковые эвенты и убрать задержку
                // Тут мини-задержка нужна, чтобы не обработать один и тот же эвент два раза
                delay(100)
                emit(ChatEvent.Internal.GetMessageLongPollingData)
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
                }
                // TODO Разобраться, как отфильтровать одинаковые эвенты и убрать задержку
                // Тут мини-задержка нужна, чтобы не обработать один и тот же эвент два раза
                delay(100)
                emit(ChatEvent.Internal.GetReactionLongPollingData)
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
                }
            }

            is ChatCommand.GetTopicsForStream -> flow {
                runCatchingNonCancellation {
                    getTopicsForStreamByIdUseCase.invoke(command.streamId)
                }.onSuccess { topics ->
                    emit(ChatEvent.Internal.TopicForStreamReceived(topics = topics))
                }.onFailure {
                    emit(ChatEvent.Internal.MinorError(errorMessageId = R.string.change_topic_error))
                }
            }

            is ChatCommand.DeleteMessage -> flow {
                runCatchingNonCancellation {
                    repository.deleteMessageById(messageId = command.messageId)
                }.onSuccess {
                    emit(ChatEvent.Internal.MessageDeletedSuccessfully(messageId = command.messageId))
                }.onFailure {
                    emit(ChatEvent.Internal.MinorError(errorMessageId = R.string.delete_message_error))
                }
            }

            is ChatCommand.EditMessage -> flow {
                runCatchingNonCancellation {
                    repository.editMessageContent(command.messageId, command.messageContent)
                }.onSuccess {
                    emit(
                        ChatEvent.Internal.MessageEditedSuccessfully(
                            messageId = command.messageId,
                            messageContent = command.messageContent
                        )
                    )
                }.onFailure {
                    emit(ChatEvent.Internal.MinorError(errorMessageId = R.string.edit_message_error))
                }
            }
        }
    }
}