package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.Operation
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.ReactionEvent
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.ReceivedMessageEventData
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.ReceivedReactionEventData
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.RegistrationForEventsData
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.repository.FakeCharRepositoryWithErrors
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.repository.FakeChatRepository
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.MyUserId
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ChatActorTest {

    private val fakeChatRepository = FakeChatRepository()
    private val fakeCharRepositoryWithErrors = FakeCharRepositoryWithErrors()
    private val streamName = "test stream name"
    private val topicName = "test topic name"

    @Test
    fun `LoadMessagesFromCache command should emit LoadMessagesFromCache event`() = runTest {
        // Given
        val messages = fakeChatRepository.getMessagesFromCache(streamName, topicName)
        val actor = ChatActor(repository = fakeChatRepository)
        // When
        val actual = actor.execute(ChatCommand.LoadMessagesFromCache(streamName, topicName))
        // Then
        assertEquals(
            ChatEvent.Internal.LoadMessagesFromCache(messages = messages),
            actual.last()
        )
    }

    @Test
    fun `LoadMessagesFromCache command should emit MinorError event if error occurs`() = runTest {
        // Given
        val actor = ChatActor(repository = fakeCharRepositoryWithErrors)
        // When
        val actual = actor.execute(ChatCommand.LoadMessagesFromCache(streamName, topicName))
        // Then
        assertEquals(
            ChatEvent.Internal.MinorError(2131820731),
            actual.last()
        )
    }

    @Test
    fun `LoadMessagesFromNetwork command should emit LoadMessagesFromNetwork event`() = runTest {
        // Given
        val messages = fakeChatRepository.getMessagesFromNetwork(streamName, topicName, anchor = "anchor", numBefore = 1, numAfter = 1)
        val actor = ChatActor(repository = fakeChatRepository)
        // When
        val actual = actor.execute(
            ChatCommand.LoadMessagesFromNetwork(
                streamName,
                topicName,
                anchor = "anchor",
                numBefore = 1,
                numAfter = 1
            )
        )
        // Then
        assertEquals(
            ChatEvent.Internal.LoadMessagesFromNetwork(messages = messages),
            actual.last()
        )
    }

    @Test
    fun `LoadMessagesFromNetwork command should emit MinorError event if error occurs`() = runTest {
        // Given
        val actor = ChatActor(repository = fakeCharRepositoryWithErrors)
        // When
        val actual = actor.execute(
            ChatCommand.LoadMessagesFromNetwork(
                streamName,
                topicName,
                anchor = "anchor",
                numBefore = 1,
                numAfter = 1
            )
        )
        // Then
        assertEquals(
            ChatEvent.Internal.MinorError(2131820606),
            actual.last()
        )
    }

    @Test
    fun `RemoveReaction command should emit MinorError event if error occurs`() = runTest {
        // Given
        val actor = ChatActor(repository = fakeCharRepositoryWithErrors)
        // When
        val actual = actor.execute(
            ChatCommand.RemoveReaction(
                messageId = 1,
                emojiName = "test emojiName",
                emojiCode = "test emojiCode"
            )
        )
        // Then
        assertEquals(
            ChatEvent.Internal.MinorError(2131820719),
            actual.last()
        )
    }

    @Test
    fun `SendReaction command should emit MinorError event if error occurs`() = runTest {
        // Given
        val actor = ChatActor(repository = fakeCharRepositoryWithErrors)
        // When
        val actual = actor.execute(
            ChatCommand.SendReaction(
                messageId = 1,
                emojiName = "test emojiName",
                emojiCode = "test emojiCode"
            )
        )
        // Then
        assertEquals(
            ChatEvent.Internal.MinorError(2131820728),
            actual.last()
        )
    }

    @Test
    fun `SendMessage command should emit MinorError event if error occurs`() = runTest {
        // Given
        val actor = ChatActor(repository = fakeCharRepositoryWithErrors)
        // When
        val actual = actor.execute(
            ChatCommand.SendMessage(
                message = "test message",
                streamName = streamName,
                topicName = topicName
            )
        )
        // Then
        assertEquals(
            ChatEvent.Internal.MinorError(2131820727),
            actual.last()
        )
    }

    @Test
    fun `SaveMessagesInCache command should emit CachedMessagesSaved event`() = runTest {
        // Given
        val actor = ChatActor(repository = fakeChatRepository)
        // When
        val actual = actor.execute(
            ChatCommand.SaveMessagesInCache(
                messages = fakeChatRepository.getTestMessages(),
                streamName = streamName,
                topicName = topicName
            )
        )
        // Then
        assertEquals(
            ChatEvent.Internal.CachedMessagesSaved,
            actual.last()
        )
    }

    @Test
    fun `RegisterForChatEvents command should emit RegistrationForChatEventsDataReceived event`() = runTest {
        // Given
        val actor = ChatActor(repository = fakeChatRepository)
        // When
        val actual = actor.execute(
            ChatCommand.RegisterForChatEvents(
               streamName = streamName,
                topicName = topicName
            )
        )
        // Then
        assertEquals(
            ChatEvent.Internal.RegistrationForChatEventsDataReceived(
                registrationForEventsData = RegistrationForEventsData(
                    messagesQueueId = "test message queue id",
                    messageLastEventId = "test message last event id",
                    reactionsQueueId = "test reaction queue id",
                    reactionLastEventId = "test reaction last event id"
                )
            ),
            actual.last()
        )
    }

    @Test
    fun `GetReactionEvents command should emit ReactionEventsDataReceived and GetReactionLongPollingData event`() = runTest {
        // Given
        val actor = ChatActor(repository = fakeChatRepository)
        // When
        val actual = actor.execute(
            ChatCommand.GetReactionEvents(
                queueId =  "test reaction queue id",
                lastEventId = "test reaction last event id",
            )
        )
        // Then
        assertEquals(
            ChatEvent.Internal.ReactionEventsDataReceived(
                receivedReactionEventData = ReceivedReactionEventData(
                    newLastEventId = "test new last event id",
                    listOf(
                        ReactionEvent(
                            emojiCode = "test emoji code",
                            emojiName = "test emoji name",
                            operation = Operation.REMOVE,
                            messageId = 1,
                            userId = MyUserId.MY_USER_ID
                        )
                    )
                )
            ),
            actual.first()
        )
        assertEquals(
            ChatEvent.Internal.GetReactionLongPollingData,
            actual.last()
        )
    }

    @Test
    fun `GetReactionEvents command should emit GetReactionLongPollingData event when error occurs`() = runTest {
        // Given
        val actor = ChatActor(repository = fakeCharRepositoryWithErrors)
        // When
        val actual = actor.execute(
            ChatCommand.GetReactionEvents(
                queueId =  "test message queue id",
                lastEventId = "test message last event id",
            )
        )
        // Then
        assertEquals(
            ChatEvent.Internal.GetReactionLongPollingData,
            actual.last()
        )
    }

    @Test
    fun `GetMessageEvents command should emit MessageEventsDataReceived and GetMessageLongPollingData event`() = runTest {
        // Given
        val actor = ChatActor(repository = fakeChatRepository)
        // When
        val actual = actor.execute(
            ChatCommand.GetMessageEvents(
                queueId =  "test message queue id",
                lastEventId = "test message last event id",
            )
        )
        // Then
        assertEquals(
            ChatEvent.Internal.MessageEventsDataReceived(
                receivedMessageEventData = ReceivedMessageEventData(
                    newLastEventId = "test new last event id",
                    newMessages =fakeChatRepository.getTestMessages()
                )
            ),
            actual.first()
        )
        assertEquals(
            ChatEvent.Internal.GetMessageLongPollingData,
            actual.last()
        )
    }

    @Test
    fun `GetMessageEvents command should emit GetMessageLongPollingData event when error occurs`() = runTest {
        // Given
        val actor = ChatActor(repository = fakeCharRepositoryWithErrors)
        // When
        val actual = actor.execute(
            ChatCommand.GetMessageEvents(
                queueId =  "test message queue id",
                lastEventId = "test message last event id",
            )
        )
        // Then
        assertEquals(
            ChatEvent.Internal.GetMessageLongPollingData,
            actual.last()
        )
    }

    @Test
    fun `LoadPagingNewerMessages command should emit LoadPagingNewerMessages and LoadingPagingDataFinished event`() = runTest {
        // Given
        val actor = ChatActor(repository = fakeChatRepository)
        // When
        val actual = actor.execute(
            ChatCommand.LoadPagingNewerMessages(
                streamName = streamName,
                topicName = topicName,
                anchor = "anchor",
                numBefore = 1,
                numAfter = 1
            )
        )
        // Then
        assertEquals(
            ChatEvent.Internal.LoadPagingNewerMessages(
                messages = fakeChatRepository.getTestMessages()
            ),
            actual.first()
        )
        assertEquals(
            ChatEvent.Internal.LoadingPagingDataFinished,
            actual.last()
        )
    }

    @Test
    fun `LoadPagingNewerMessages command should emit MinorError and LoadingPagingDataFinished event when error occurs`() = runTest {
        // Given
        val actor = ChatActor(repository = fakeCharRepositoryWithErrors)
        // When
        val actual = actor.execute(
            ChatCommand.LoadPagingNewerMessages(
                streamName = streamName,
                topicName = topicName,
                anchor = "anchor",
                numBefore = 1,
                numAfter = 1
            )
        )
        // Then
        assertEquals(
            ChatEvent.Internal.MinorError(2131820606),
            actual.first()
        )
        assertEquals(
            ChatEvent.Internal.LoadingPagingDataFinished,
            actual.last()
        )
    }

    @Test
    fun `LoadPagingOlderMessages command should emit LoadPagingOlderMessages and LoadingPagingDataFinished event`() = runTest {
        // Given
        val actor = ChatActor(repository = fakeChatRepository)
        // When
        val actual = actor.execute(
            ChatCommand.LoadPagingOlderMessages(
                streamName = streamName,
                topicName = topicName,
                anchor = "anchor",
                numBefore = 1,
                numAfter = 1
            )
        )
        // Then
        assertEquals(
            ChatEvent.Internal.LoadPagingOlderMessages(
                messages = fakeChatRepository.getTestMessages()
            ),
            actual.first()
        )
        assertEquals(
            ChatEvent.Internal.LoadingPagingDataFinished,
            actual.last()
        )
    }

    @Test
    fun `LoadPagingOlderMessages command should emit MinorError and LoadingPagingDataFinished event when error occurs`() = runTest {
        // Given
        val actor = ChatActor(repository = fakeCharRepositoryWithErrors)
        // When
        val actual = actor.execute(
            ChatCommand.LoadPagingOlderMessages(
                streamName = streamName,
                topicName = topicName,
                anchor = "anchor",
                numBefore = 1,
                numAfter = 1
            )
        )
        // Then
        assertEquals(
            ChatEvent.Internal.MinorError(2131820606),
            actual.first()
        )
        assertEquals(
            ChatEvent.Internal.LoadingPagingDataFinished,
            actual.last()
        )
    }


}