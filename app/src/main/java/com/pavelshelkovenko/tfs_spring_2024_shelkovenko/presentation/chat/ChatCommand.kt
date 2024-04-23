package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat

sealed interface ChatCommand {

    data class LoadInitialMessages(
        val streamName: String,
        val topicName: String,
        val anchor: String = "first_unread",
        val numBefore: Int,
        val numAfter: Int,
    ): ChatCommand

    data class LoadPagingNewerMessages(
        val streamName: String,
        val topicName: String,
        val anchor: String,
        val numBefore: Int,
        val numAfter: Int,
    ): ChatCommand

    data class LoadPagingOlderMessages(
        val streamName: String,
        val topicName: String,
        val anchor: String,
        val numBefore: Int,
        val numAfter: Int,
    ): ChatCommand

    data class SendMessage(
        val message: String,
        val topicName: String,
        val streamName: String,
    ): ChatCommand

    data class SendReaction(
        val messageId: Int,
        val emojiName: String,
        val emojiCode: String,
    ): ChatCommand

    data class RemoveReaction(
        val messageId: Int,
        val emojiName: String,
        val emojiCode: String,
    ): ChatCommand

    data class RegisterForChatEvents(
        val streamName: String,
        val topicName: String,
    ): ChatCommand

    data class GetMessageEvents(
        val queueId: String,
        val lastEventId: String,
    ): ChatCommand

    data class GetReactionEvents(
        val queueId: String,
        val lastEventId: String,
    ): ChatCommand

}