package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Message
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Topic
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.ReceivedMessageEventData
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.ReceivedReactionEventData
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.RegistrationForEventsData

sealed interface ChatEvent {

    sealed interface Ui : ChatEvent {
        data class StartProcess(val streamName: String, val topicName: String) : Ui

        data class RegisterForChatEvents(val streamName: String, val topicName: String) : Ui

        data class SendMessage(
            val message: String,
            val topicName: String,
            val streamName: String
        ) : Ui

        data class SendReaction(
            val messageId: Int,
            val emojiName: String,
            val emojiCode: String
        ) : Ui

        data class RemoveReaction(
            val messageId: Int,
            val emojiName: String,
            val emojiCode: String
        ) : Ui

        data class ReloadData(val streamName: String, val topicName: String) : Ui

        data class LoadPagingNewerMessages(val streamName: String, val topicName: String): Ui

        data class LoadPagingOlderMessages(val streamName: String, val topicName: String): Ui

        data class OnEmojiClick(
            val messageId: Int,
            val emojiCode: String,
            val emojiName: String
        ): Ui

        data class ClosingChat(val streamName: String, val topicName: String): Ui

        data class OnTopicChooserClick(val streamId: Int): Ui

        data class DeleteMessage(val messageId: Int): Ui

        data class EditMessage(val newMessageContent: String, val messageId: Int): Ui

        data class ActivateEditingMode(
            val editingMessageId: Int,
            val oldMessageContent: String,
            val newMessageContent: String,
        ): Ui

        data object DeactivateEditingMode: Ui

        data class SaveNewEditingMessageContent(val newMessageContent: String): Ui

    }

    sealed interface Internal : ChatEvent {

        data class LoadMessagesFromNetwork(val messages: List<Message>) : Internal

        data class LoadMessagesFromCache(
            val messages: List<Message>,
            val streamName: String,
            val topicName: String,
        ) : Internal

        data class LoadPagingNewerMessages(val messages: List<Message>) : Internal

        data class LoadPagingOlderMessages(val messages: List<Message>) : Internal

        data class Error(val errorMessageId: Int) : Internal

        data class MinorError(val errorMessageId: Int) : Internal

        data class ErrorLoadingFromCache(
            val streamName: String,
            val topicName: String,
        ) : Internal

        data class PagingError(val errorMessageId: Int) : Internal

        data class RegistrationForChatEventsDataReceived(
            val registrationForEventsData: RegistrationForEventsData
        ) : Internal

        data class MessageEventsDataReceived(
            val receivedMessageEventData: ReceivedMessageEventData
        ) : Internal

        data class ReactionEventsDataReceived(
            val receivedReactionEventData: ReceivedReactionEventData
        ) : Internal

        data class TopicForStreamReceived(val topics: List<Topic>): Internal

        data object LoadingPagingDataFinished: Internal

        data object GetMessageLongPollingData: Internal

        data object GetReactionLongPollingData: Internal

        data object CachedMessagesSaved: Internal

        data class MessageDeletedSuccessfully(val messageId: Int): Internal

        data class MessageEditedSuccessfully(val messageId: Int, val messageContent: String): Internal
    }
}