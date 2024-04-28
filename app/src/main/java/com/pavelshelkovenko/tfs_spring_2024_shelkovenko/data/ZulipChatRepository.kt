package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.utils.NarrowBuilderHelper
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.utils.toMessage
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.utils.toOperation
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.ChatRepository
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Message
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.ReactionEvent
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.ReceivedMessageEventData
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.ReceivedReactionEventData
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.RegistrationForEventsData

class ZulipChatRepository(
    private val zulipApi: ZulipApi
): ChatRepository {
    override suspend fun getMessages(
        streamName: String,
        topicName: String,
        anchor: String,
        numBefore: Int,
        numAfter: Int
    ): List<Message> {
        // Пока сделал так, потом через Di буду в конструктор инжектить NarrowBuilderHelper
        val narrow = NarrowBuilderHelper().getNarrowArrayWithObjectStructure(topicName, streamName)
        val response = zulipApi.getMessages(
            anchor = anchor,
            numBefore = numBefore,
            numAfter = numAfter,
            narrow = narrow
        )
        val messagesDto = response.messages
        val resultMessages = messagesDto.map { messageDto ->
            messageDto.toMessage()
        }
        return resultMessages
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

    override suspend fun sendMessage(streamName: String, topicName: String, message: String) {
        zulipApi.sendMessage(
            streamName = streamName,
            topicName = topicName,
            message = message
        )
    }


    override suspend fun registerForEvents(streamName: String, topicName: String): RegistrationForEventsData {
        val narrow = NarrowBuilderHelper().getNarrowArrayWithArrayStructure(
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
            messageEventDto.message.toMessage()
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