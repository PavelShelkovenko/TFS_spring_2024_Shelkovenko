package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.actor.repository

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.AccountInfo
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Message
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.Operation
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.ReactionEvent
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.ReceivedMessageEventData
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.ReceivedReactionEventData
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.RegistrationForEventsData
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.repository.ChatRepository
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.TestMessageGenerator

class FakeChatRepository: ChatRepository {

    private val testMessageGenerator = TestMessageGenerator()
    private val accountInfo = AccountInfo()

    override suspend fun getMessagesFromNetwork(
        streamName: String,
        topicName: String,
        anchor: String,
        numBefore: Int,
        numAfter: Int
    ): List<Message> = testMessageGenerator.generateTestMessages()

    override suspend fun getMessagesFromCache(
        streamName: String,
        topicName: String
    ): List<Message> = testMessageGenerator.generateTestMessages()

    override suspend fun getPagingMessages(
        streamName: String,
        topicName: String,
        anchor: String,
        numBefore: Int,
        numAfter: Int
    ): List<Message> = testMessageGenerator.generateTestMessages()

    override suspend fun saveMessagesInCache(
        messages: List<Message>,
        streamName: String,
        topicName: String
    ) = Unit

    override suspend fun sendMessage(streamName: String, topicName: String, message: String) = Unit

    override suspend fun sendReaction(messageId: Int, emojiName: String, emojiCode: String) = Unit

    override suspend fun removeReaction(messageId: Int, emojiName: String, emojiCode: String) = Unit

    override suspend fun registerForEvents(
        streamName: String,
        topicName: String
    ): RegistrationForEventsData = RegistrationForEventsData(
        messagesQueueId = "test message queue id",
        messageLastEventId = "test message last event id",
        reactionsQueueId = "test reaction queue id",
        reactionLastEventId = "test reaction last event id"
    )

    override suspend fun getMessageEvents(
        queueId: String,
        lastEventId: String
    ): ReceivedMessageEventData = ReceivedMessageEventData(
        newLastEventId = "test new last event id",
        newMessages = testMessageGenerator.generateTestMessages()
    )

    override suspend fun getReactionEvents(
        queueId: String,
        lastEventId: String
    ): ReceivedReactionEventData = ReceivedReactionEventData(
        newLastEventId = "test new last event id",
        reactionEvents = listOf(
            ReactionEvent(
                emojiCode = "test emoji code",
                emojiName = "test emoji name",
                operation = Operation.REMOVE,
                messageId = 1,
                userId = accountInfo.userId
            )
        )
    )

    fun getTestMessages(): List<Message> = testMessageGenerator.generateTestMessages()
}