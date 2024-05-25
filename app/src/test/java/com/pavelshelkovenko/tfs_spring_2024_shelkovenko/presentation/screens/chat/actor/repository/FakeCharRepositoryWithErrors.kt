package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.actor.repository

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Message
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.ReceivedMessageEventData
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.ReceivedReactionEventData
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.RegistrationForEventsData
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.repository.ChatRepository

class FakeCharRepositoryWithErrors: ChatRepository {
    override suspend fun getMessagesFromNetwork(
        streamName: String,
        topicName: String,
        anchor: String,
        numBefore: Int,
        numAfter: Int
    ): List<Message> = throw IllegalStateException("Test error")

    override suspend fun getMessagesFromCache(
        streamName: String,
        topicName: String
    ): List<Message> = throw IllegalStateException("Test error")

    override suspend fun getPagingMessages(
        streamName: String,
        topicName: String,
        anchor: String,
        numBefore: Int,
        numAfter: Int
    ): List<Message> = throw IllegalStateException("Test error")

    override suspend fun saveMessagesInCache(
        messages: List<Message>,
        streamName: String,
        topicName: String
    ) {
        throw IllegalStateException("Test error")
    }

    override suspend fun sendMessage(streamName: String, topicName: String, message: String) {
        throw IllegalStateException("Test error")
    }

    override suspend fun sendReaction(messageId: Int, emojiName: String, emojiCode: String) {
        throw IllegalStateException("Test error")
    }

    override suspend fun removeReaction(messageId: Int, emojiName: String, emojiCode: String) {
        throw IllegalStateException("Test error")
    }

    override suspend fun deleteMessageById(messageId: Int) {
        throw IllegalStateException("Test error")
    }

    override suspend fun editMessageContent(messageId: Int, newMessageContent: String) {
        throw IllegalStateException("Test error")
    }

    override suspend fun registerForEvents(
        streamName: String,
        topicName: String
    ): RegistrationForEventsData {
        throw IllegalStateException("Test error")
    }

    override suspend fun getMessageEvents(
        queueId: String,
        lastEventId: String
    ): ReceivedMessageEventData {
        throw IllegalStateException("Test error")
    }

    override suspend fun getReactionEvents(
        queueId: String,
        lastEventId: String
    ): ReceivedReactionEventData {
        throw IllegalStateException("Test error")
    }
}