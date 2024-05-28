package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.repository

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.AccountInfo
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.NarrowBuilderHelper
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.dao.ChatDao
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.mappers.toMessageDbo
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.mappers.toMessageDomain
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.mappers.toOperation
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.ZulipApi
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Message
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.ReactionEvent
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.ReceivedMessageEventData
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.ReceivedReactionEventData
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.RegistrationForEventsData
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.repository.ChatRepository
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val zulipApi: ZulipApi,
    private val narrowBuilderHelper: NarrowBuilderHelper,
    private val chatDao: ChatDao,
    private val accountInfo: AccountInfo,
) : ChatRepository {

    override suspend fun getMessagesFromNetwork(
        streamName: String,
        topicName: String,
        anchor: String,
        numBefore: Int,
        numAfter: Int
    ): List<Message> {
        val narrow = narrowBuilderHelper.getNarrowArrayWithObjectStructure(topicName, streamName)
        val response = zulipApi.getMessages(
            anchor = anchor,
            numBefore = numBefore,
            numAfter = numAfter,
            narrow = narrow
        )
        val messagesDto = response.messages
        val resultMessages = messagesDto.map { messageDto ->
            messageDto.toMessageDomain(accountInfo)
        }
        return resultMessages
    }

    override suspend fun getPagingMessages(
        streamName: String,
        topicName: String,
        anchor: String,
        numBefore: Int,
        numAfter: Int
    ): List<Message> = getMessagesFromNetwork(
        streamName = streamName,
        topicName = topicName,
        anchor = anchor,
        numBefore = numBefore,
        numAfter = numAfter
    )

    override suspend fun getMessagesFromCache(
        streamName: String,
        topicName: String,
    ): List<Message> = chatDao.getAll(streamName, topicName).map { messageDbo -> messageDbo.toMessageDomain() }

    override suspend fun saveMessagesInCache(
        messages: List<Message>,
        streamName: String,
        topicName: String
    ) {
        val messagesForCaching = messages.map { it.toMessageDbo(streamName, topicName) }
        chatDao.insertNewAndDeleteOldMessages(
            messages = messagesForCaching,
            streamName = streamName,
            topicName = topicName
        )
    }


    override suspend fun sendReaction(messageId: Int, emojiName: String, emojiCode: String) {
        zulipApi.sendReaction(
            messageId = messageId,
            emojiName = emojiName,
            emojiCode = emojiCode
        )
    }

    override suspend fun removeReaction(messageId: Int, emojiName: String, emojiCode: String) {
        zulipApi.removeReaction(
            messageId = messageId,
            emojiName = emojiName,
            emojiCode = emojiCode
        )
    }

    override suspend fun deleteMessageById(messageId: Int) {
        zulipApi.deleteMessageById(messageId)
        chatDao.deleteMessageById(messageId)
    }

    override suspend fun editMessageContent(
        messageId: Int,
        newMessageContent: String
    ) {
        zulipApi.editMessageContent(messageId, newMessageContent)
        chatDao.updateMessage(messageId, newMessageContent)
    }

    override suspend fun sendMessage(streamName: String, topicName: String, message: String) {
        zulipApi.sendMessage(
            streamName = streamName,
            topicName = topicName,
            message = message
        )
    }


    override suspend fun registerForEvents(
        streamName: String,
        topicName: String
    ): RegistrationForEventsData {
        val narrow = narrowBuilderHelper.getNarrowArrayWithArrayStructure(
            streamName = streamName, topicName = topicName
        )
        val registrationForMessages = zulipApi.registerMessageEvents(narrow)
        val registrationForReactions = zulipApi.registerReactionEvents(narrow)
        return RegistrationForEventsData(
            messagesQueueId = registrationForMessages.queueId,
            messageLastEventId = registrationForMessages.lastEventId,
            reactionsQueueId = registrationForReactions.queueId,
            reactionLastEventId = registrationForReactions.lastEventId
        )
    }

    override suspend fun getMessageEvents(
        queueId: String,
        lastEventId: String
    ): ReceivedMessageEventData {
        val messageEventsDto = zulipApi.getMessageEvents(
            queueId = queueId,
            lastEventId = lastEventId
        ).messageEvents

        val newLastEventId = messageEventsDto.last().id

        val newMessages = messageEventsDto.map { messageEventDto ->
            messageEventDto.message.toMessageDomain(accountInfo)
        }

        return ReceivedMessageEventData(
            newLastEventId = newLastEventId,
            newMessages = newMessages
        )
    }

    override suspend fun getReactionEvents(
        queueId: String,
        lastEventId: String
    ): ReceivedReactionEventData {
        val reactionEventsDto = zulipApi.getReactionEvents(
            queueId = queueId,
            lastEventId = lastEventId
        ).reactionEvents
        val newLastEventId = reactionEventsDto.last().id
        val reactionEvents = reactionEventsDto.map { reactionEventDto ->
            ReactionEvent(
                emojiCode = reactionEventDto.emojiCode,
                emojiName = reactionEventDto.emojiName,
                operation = reactionEventDto.operationDto.toOperation(),
                messageId = reactionEventDto.messageId,
                userId = reactionEventDto.userId
            )
        }
        return ReceivedReactionEventData(
            newLastEventId = newLastEventId,
            reactionEvents = reactionEvents
        )
    }
}