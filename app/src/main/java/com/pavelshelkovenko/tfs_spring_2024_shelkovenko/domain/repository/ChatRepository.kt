package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.repository

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Message
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.ReceivedMessageEventData
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.ReceivedReactionEventData
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.RegistrationForEventsData

interface ChatRepository {

    suspend fun getMessagesFromNetwork(
        streamName: String,
        topicName: String,
        anchor: String,
        numBefore: Int,
        numAfter: Int,
    ): List<Message>

    suspend fun getMessagesFromCache(
        streamName: String,
        topicName: String,
    ): List<Message>

    suspend fun getPagingMessages(
        streamName: String,
        topicName: String,
        anchor: String,
        numBefore: Int,
        numAfter: Int
    ): List<Message>

    suspend fun saveMessagesInCache(
        messages: List<Message>,
        streamName: String,
        topicName: String
    )

    suspend fun sendMessage(streamName: String, topicName: String, message: String)


    suspend fun sendReaction(messageId: Int, emojiName: String, emojiCode: String)


    suspend fun removeReaction(messageId: Int, emojiName: String, emojiCode: String)

    suspend fun deleteMessageById(messageId: Int)

    suspend fun editMessageContent(messageId: Int, newMessageContent: String)

    suspend fun registerForEvents(
        streamName: String,
        topicName: String
    ): RegistrationForEventsData


    suspend fun getMessageEvents(
        queueId: String,
        lastEventId: String,
    ): ReceivedMessageEventData

    suspend fun getReactionEvents(
        queueId: String,
        lastEventId: String,
    ): ReceivedReactionEventData
}