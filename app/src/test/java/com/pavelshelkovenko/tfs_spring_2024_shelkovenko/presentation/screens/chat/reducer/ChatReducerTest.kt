package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.reducer

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.AccountInfo
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Message
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Reaction
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.Operation
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.ReactionEvent
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.ReceivedMessageEventData
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.ReceivedReactionEventData
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.RegistrationForEventsData
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.ChatCommand
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.ChatEffect
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.ChatEvent
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.ChatReducer
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.ChatState
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.LongPollingInfoHolder
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.PaginationInfoHolder
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.date.MessageDateTimeDelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.message.MessageDelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.message.received_message.ReceivedMessageDelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.message.send_message.SendMessageDelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.TestMessageGenerator
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class ChatReducerTest {

    private val longPollingInfoHolder = LongPollingInfoHolder()
    private val paginationInfoHolder = PaginationInfoHolder()
    private val testMessageGenerator = TestMessageGenerator()
    private val accountInfo = AccountInfo()
    private lateinit var reducer: ChatReducer
    private val countOfMessagesToDownload = 25
    private val streamName = "test stream name"
    private val topicName = "test topic name"


    @Before
    fun setup() {
        reducer = ChatReducer(longPollingInfoHolder, paginationInfoHolder, accountInfo)
    }

    @Test
    fun `reduce StartProcess event should contains commands LoadMessagesFromCache AND LoadMessagesFromNetwork`() {
        // Given
        val state = ChatState.Initial
        val event = ChatEvent.Ui.StartProcess(streamName, topicName)
        // When
        val actual = reducer.reduce(event, state)
        // Then
        assertEquals(
            listOf(
                ChatCommand.LoadMessagesFromCache(streamName, topicName),
                ChatCommand.LoadMessagesFromNetwork(
                    streamName = streamName,
                    topicName = topicName,
                    anchor = paginationInfoHolder.newestAnchor,
                    numBefore = countOfMessagesToDownload * 2,
                    numAfter = countOfMessagesToDownload
                )
            ),
            actual.commands
        )
    }

    @Test
    fun `reduce ReloadData event should set state to Loading AND contains LoadMessagesFromNetwork command`() {
        // Given
        val state = ChatState.Initial
        val event = ChatEvent.Ui.ReloadData(streamName, topicName)
        // When
        val actual = reducer.reduce(event, state)
        // Then
        assertEquals(ChatState.Loading, actual.state)
        assertEquals(
            listOf(
                ChatCommand.LoadMessagesFromNetwork(
                    streamName = streamName,
                    topicName = topicName,
                    anchor = paginationInfoHolder.newestAnchor,
                    numBefore = countOfMessagesToDownload * 2,
                    numAfter = countOfMessagesToDownload
                )
            ),
            actual.commands
        )
    }

    @Test
    fun `reduce LoadPagingNewerMessages event should contain command LoadPagingNewerMessages`() {
        // Given
        val state = ChatState.Content(emptyList())
        val event = ChatEvent.Ui.LoadPagingNewerMessages(streamName, topicName)
        val reducer = ChatReducer(
            longPollingInfoHolder,
            paginationInfoHolder.copy(
                isLoadingPagingData = false,
                hasLoadedNewestMessage = false
            ),
            accountInfo
        )
        // When
        val actual = reducer.reduce(event, state)
        // Then
        assertEquals(
            listOf(
                ChatCommand.LoadPagingNewerMessages(
                    streamName = streamName,
                    topicName = topicName,
                    anchor = paginationInfoHolder.newestAnchor,
                    numBefore = 0,
                    numAfter = countOfMessagesToDownload
                )
            ),
            actual.commands
        )
    }

    @Test
    fun `reduce LoadPagingNewerMessages event WHEN isLoadingNextPage is true should not contain any commands`() {
        // Given
        val state = ChatState.Content(emptyList())
        val event = ChatEvent.Ui.LoadPagingNewerMessages(streamName, topicName)
        val reducer = ChatReducer(
            longPollingInfoHolder,
            paginationInfoHolder.copy(isLoadingPagingData = true),
            accountInfo
        )
        // When
        val actual = reducer.reduce(event, state)
        // Then
        assertEquals(
            emptyList<ChatCommand>(),
            actual.commands
        )
    }

    @Test
    fun `reduce LoadPagingNewerMessages event WHEN hasLoadedNewestMessage is true should not contain any commands`() {
        val state = ChatState.Content(emptyList())
        val event = ChatEvent.Ui.LoadPagingNewerMessages(streamName, topicName)
        val reducer = ChatReducer(
            longPollingInfoHolder,
            paginationInfoHolder.copy(hasLoadedNewestMessage = true),
            accountInfo
        )
        // When
        val actual = reducer.reduce(event, state)
        // Then
        assertEquals(
            emptyList<ChatCommand>(),
            actual.commands
        )
    }

    @Test
    fun `reduce OnEmojiClick event WHEN clicked on own reaction should contain command RemoveReaction`() {
        // Given
        val messages = testMessageGenerator.generateTestMessages()
        val state = ChatState.Content(emptyList())
        val eventForLoadingMessage = ChatEvent.Internal.LoadMessagesFromNetwork(messages)
        val event = ChatEvent.Ui.OnEmojiClick(
            messageId = messages.first().id,
            emojiCode = messages.first().reactions.first().emojiCode,
            emojiName = messages.first().reactions.first().emojiName
        )
        // When
        val reducerWithMessages = reducer.reduce(eventForLoadingMessage, state)
        val actual = reducer.reduce(event, reducerWithMessages.state)
        // Then
        assertEquals(
            listOf(
                ChatCommand.RemoveReaction(
                    messageId = messages.first().id,
                    emojiCode = messages.first().reactions.first().emojiCode,
                    emojiName = messages.first().reactions.first().emojiName
                )
            ),
            actual.commands
        )
    }

    @Test
    fun `reduce OnEmojiClick event WHEN clicked on another reaction should contain command SendReaction`() {
        // Given
        val messages = testMessageGenerator.generateTestMessages()
        val state = ChatState.Content(emptyList())
        val eventForLoadingMessage = ChatEvent.Internal.LoadMessagesFromNetwork(messages)
        val event = ChatEvent.Ui.OnEmojiClick(
            messageId = messages.first().id,
            emojiCode = messages.first().reactions.last().emojiCode,
            emojiName = messages.first().reactions.last().emojiName
        )
        // When
        val reducerWithMessages = reducer.reduce(eventForLoadingMessage, state)
        val actual = reducer.reduce(event, reducerWithMessages.state)
        // Then
        assertEquals(
            listOf(
                ChatCommand.SendReaction(
                    messageId = messages.first().id,
                    emojiCode = messages.first().reactions.last().emojiCode,
                    emojiName = messages.first().reactions.last().emojiName
                )
            ),
            actual.commands
        )
    }

    @Test
    fun `reduce CachedMessageSaved event should contain effect CloseChat`() {
        // Given
        val state = ChatState.Content(emptyList())
        val event = ChatEvent.Internal.CachedMessagesSaved
        // When
        val actual = reducer.reduce(event, state)
        // Then
        assertEquals(
            listOf(
                ChatEffect.CloseChat
            ),
            actual.effects
        )
    }

    @Test
    fun `reduce GetReactionLongPollingData event should contain command GetReactionEvents`() {
        // Given
        val state = ChatState.Content(emptyList())
        val event = ChatEvent.Internal.GetReactionLongPollingData
        // When
        val actual = reducer.reduce(event, state)
        // Then
        assertEquals(
            listOf(
                ChatCommand.GetReactionEvents(
                    queueId = longPollingInfoHolder.reactionQueueId,
                    lastEventId = longPollingInfoHolder.lastReactionEventId
                )
            ),
            actual.commands
        )
    }

    @Test
    fun `reduce GetMessageLongPollingData event should contain command GetMessageEvents`() {
        // Given
        val state = ChatState.Content(emptyList())
        val event = ChatEvent.Internal.GetMessageLongPollingData
        // When
        val actual = reducer.reduce(event, state)
        // Then
        assertEquals(
            listOf(
                ChatCommand.GetMessageEvents(
                    queueId = longPollingInfoHolder.messageQueueId,
                    lastEventId = longPollingInfoHolder.lastMessageEventId
                )
            ),
            actual.commands
        )
    }

    @Test
    fun `reduce LoadPagingOlderMessages event should set to state new messages`() {
        // Given
        val messages = testMessageGenerator.generateTestMessages()
        val listOfDelegateItem = testMessageGenerator.generateTestMessageDelegateItem()
        val state = ChatState.Content(emptyList())
        val event = ChatEvent.Internal.LoadPagingOlderMessages(
            messages = messages
        )
        // When
        val actual = reducer.reduce(event, state)
        // Then
        assertEquals(
            listOfDelegateItem.size,
            (actual.state as ChatState.Content).messages.size
        )
        assertEquals(
            (listOfDelegateItem[0] as MessageDateTimeDelegateItem).value,
            (actual.state as ChatState.Content).messages[0].content()
        )
        assertEquals(
            (listOfDelegateItem[1] as SendMessageDelegateItem).value,
            (actual.state as ChatState.Content).messages[1].content()
        )
        assertEquals(
            (listOfDelegateItem[2] as ReceivedMessageDelegateItem).value,
            (actual.state as ChatState.Content).messages[2].content()
        )
    }

    @Test
    fun `reduce LoadPagingNewerMessages event should set to state new messages`() {
        // Given
        val messages = testMessageGenerator.generateTestMessages()
        val listOfDelegateItem = testMessageGenerator.generateTestMessageDelegateItem()
        val state = ChatState.Content(emptyList())
        val event = ChatEvent.Internal.LoadPagingNewerMessages(messages = messages)
        // When
        val actual = reducer.reduce(event, state)
        // Then
        assertEquals(
            listOfDelegateItem.size,
            (actual.state as ChatState.Content).messages.size
        )
        assertEquals(
            (listOfDelegateItem[0] as MessageDateTimeDelegateItem).value,
            (actual.state as ChatState.Content).messages[0].content()
        )
        assertEquals(
            (listOfDelegateItem[1] as SendMessageDelegateItem).value,
            (actual.state as ChatState.Content).messages[1].content()
        )
        assertEquals(
            (listOfDelegateItem[2] as ReceivedMessageDelegateItem).value,
            (actual.state as ChatState.Content).messages[2].content()
        )
    }

    @Test
    fun `reduce Error event should produce error state`() {
        // Given
        val state = ChatState.Content(emptyList())
        val event = ChatEvent.Internal.Error(1)
        // When
        val actual = reducer.reduce(event, state)
        // Then
        assertEquals(
            ChatState.Error(1),
            actual.state
        )
    }

    @Test
    fun `reduce MinorError event should produce MinorError effect`() {
        // Given
        val state = ChatState.Content(emptyList())
        val event = ChatEvent.Internal.MinorError(1)
        // When
        val actual = reducer.reduce(event, state)
        // Then
        assertEquals(
            listOf(
                ChatEffect.MinorError(1),
            ),
            actual.effects
        )
    }


    @Test
    fun `reduce LoadMessagesFromCache event should set to state new messages and update newestAnchor in pagingInfoHolder`() {
        // Given
        val messages = testMessageGenerator.generateTestMessages()
        val listOfDelegateItem = testMessageGenerator.generateTestMessageDelegateItem()
        val state = ChatState.Content(emptyList())
        val event = ChatEvent.Internal.LoadMessagesFromCache(
            messages = messages,
            streamName = streamName,
            topicName = topicName
        )
        // When
        val actual = reducer.reduce(event, state)
        // Then
        assertEquals(
            listOfDelegateItem.size,
            (actual.state as ChatState.Content).messages.size
        )
        assertEquals(
            (listOfDelegateItem[0] as MessageDateTimeDelegateItem).value,
            (actual.state as ChatState.Content).messages[0].content()
        )
        assertEquals(
            (listOfDelegateItem[1] as SendMessageDelegateItem).value,
            (actual.state as ChatState.Content).messages[1].content()
        )
        assertEquals(
            (listOfDelegateItem[2] as ReceivedMessageDelegateItem).value,
            (actual.state as ChatState.Content).messages[2].content()
        )
        assertEquals(
            messages.last().id.toString(),
            paginationInfoHolder.newestAnchor
        )
    }

    @Test
    fun `reduce LoadMessagesFromCache event WHEN messages are empty should set state to Loading`() {
        // Given
        val messages = emptyList<Message>()
        val state = ChatState.Content(emptyList())
        val event = ChatEvent.Internal.LoadMessagesFromCache(
            messages = messages,
            streamName = streamName,
            topicName = topicName
        )
        // When
        val actual = reducer.reduce(event, state)
        // Then
        assertEquals(
            ChatState.Loading,
            actual.state
        )
    }

    @Test
    fun `reduce LoadMessagesFromNetwork event should set to state new messages and update paginationInfoHolder`() {
        // Given
        val messages = testMessageGenerator.generateTestMessages()
        val listOfDelegateItem = testMessageGenerator.generateTestMessageDelegateItem()
        val state = ChatState.Content(emptyList())
        val event = ChatEvent.Internal.LoadMessagesFromNetwork(messages = messages)
        // When
        val actual = reducer.reduce(event, state)
        // Then
        assertEquals(
            listOfDelegateItem.size,
            (actual.state as ChatState.Content).messages.size
        )
        assertEquals(
            (listOfDelegateItem[0] as MessageDateTimeDelegateItem).value,
            (actual.state as ChatState.Content).messages[0].content()
        )
        assertEquals(
            (listOfDelegateItem[1] as SendMessageDelegateItem).value,
            (actual.state as ChatState.Content).messages[1].content()
        )
        assertEquals(
            (listOfDelegateItem[2] as ReceivedMessageDelegateItem).value,
            (actual.state as ChatState.Content).messages[2].content()
        )
        assertEquals(
            messages.first().id.toString(),
            paginationInfoHolder.oldestAnchor
        )
        assertEquals(
            messages.last().id.toString(),
            paginationInfoHolder.newestAnchor
        )
    }

    @Test
    fun `reduce RegistrationForChatEventsDataReceived event should update longPollingInfoHolder AND should contain commands GetMessageEvents GetReactionEvents`() {
        // Given
        val testMessageQueueId = "test message queue id"
        val testMessageLastEventId = "test message last event id"
        val testReactionQueueId = "test reaction queue id"
        val testReactionLastEventId = "test reaction last event id"

        val testRegistrationForEventData = RegistrationForEventsData(
            messagesQueueId = testMessageQueueId,
            messageLastEventId = testMessageLastEventId,
            reactionsQueueId = testReactionQueueId,
            reactionLastEventId = testReactionLastEventId
        )

        val state = ChatState.Content(emptyList())
        val event =
            ChatEvent.Internal.RegistrationForChatEventsDataReceived(testRegistrationForEventData)
        // When
        val actual = reducer.reduce(event, state)
        // Then
        assertEquals(
            listOf(
                ChatCommand.GetMessageEvents(
                    queueId = testMessageQueueId,
                    lastEventId = testMessageLastEventId
                ),
                ChatCommand.GetReactionEvents(
                    queueId = testReactionQueueId,
                    lastEventId = testReactionLastEventId
                )
            ),
            actual.commands
        )
        assertEquals(
            testMessageQueueId,
            longPollingInfoHolder.messageQueueId
        )
        assertEquals(
            testMessageLastEventId,
            longPollingInfoHolder.lastMessageEventId
        )
        assertEquals(
            testReactionQueueId,
            longPollingInfoHolder.reactionQueueId
        )
        assertEquals(
            testReactionLastEventId,
            longPollingInfoHolder.lastReactionEventId
        )
    }

    @Test
    fun `reduce MessageEventsDataReceived should set to state new messages and update longPollingInfoHolder`() {
        // Given
        val testMessages = testMessageGenerator.generateTestMessages()
        val listOfDelegateItem = testMessageGenerator.generateTestMessageDelegateItem()
        val testNewLastEventId = "test last event id"
        val testReceivedMessageEventData = ReceivedMessageEventData(testNewLastEventId, testMessages)

        val state = ChatState.Content(emptyList())
        val event = ChatEvent.Internal.MessageEventsDataReceived(testReceivedMessageEventData)
        // When
        val actual = reducer.reduce(event, state)
        // Then
        assertEquals(
            testNewLastEventId,
            longPollingInfoHolder.lastMessageEventId
        )
        assertEquals(
            listOf(
                ChatEffect.NewMessageReceived
            ),
            actual.effects
        )
        assertEquals(
            listOfDelegateItem.size,
            (actual.state as ChatState.Content).messages.size
        )
        assertEquals(
            (listOfDelegateItem[0] as MessageDateTimeDelegateItem).value,
            (actual.state as ChatState.Content).messages[0].content()
        )
        assertEquals(
            (listOfDelegateItem[1] as SendMessageDelegateItem).value,
            (actual.state as ChatState.Content).messages[1].content()
        )
        assertEquals(
            (listOfDelegateItem[2] as ReceivedMessageDelegateItem).value,
            (actual.state as ChatState.Content).messages[2].content()
        )
    }


    @Test
    fun `reduce ReactionEventsDataReceived when (operation is remove AND count more than 1) should set reaction to (count - 1) and update state with new reactions AND update longPollingInfoHolder`() {
        // Given
        val testMessages = testMessageGenerator.generateTestMessages()
        val listOfDelegateItem = testMessageGenerator.generateTestMessageDelegateItem()
        val testNewLastEventId = "test last event id"
        val testReactionEvent = ReactionEvent(
            emojiCode = testMessages.first().reactions.first().emojiCode,
            emojiName = testMessages.first().reactions.first().emojiName,
            operation = Operation.REMOVE,
            messageId = testMessages.first().id,
            userId = accountInfo.userId
        )
        val testReceivedReactionEventData =
            ReceivedReactionEventData(testNewLastEventId, listOf(testReactionEvent))

        val state = ChatState.Initial
        val event = ChatEvent.Internal.ReactionEventsDataReceived(testReceivedReactionEventData)
        val eventForLoadingMessage =
            ChatEvent.Internal.LoadMessagesFromNetwork(testMessageGenerator.generateTestMessages())
        // When
        val reducerWithMessages = reducer.reduce(eventForLoadingMessage, state)
        val actual = reducer.reduce(event, reducerWithMessages.state)
        // Then
        val actualReactionCount = ((actual.state as ChatState.Content).messages[1] as MessageDelegateItem).reactionList.first().count
        val expectedReactionCount = (listOfDelegateItem[1] as SendMessageDelegateItem).value.reactionList.first().count - 1
        assertEquals(
            testNewLastEventId,
            longPollingInfoHolder.lastReactionEventId
        )
        assertEquals(
            listOfDelegateItem.size,
            (actual.state as ChatState.Content).messages.size
        )
        assertEquals(
            expectedReactionCount,
            actualReactionCount
        )
    }

    @Test
    fun `reduce ReactionEventsDataReceived when (operation is remove AND count is 1) should remove reaction and update state with new reactions AND update longPollingInfoHolder`() {
        // Given
        val myReaction = testMessageGenerator.generateMyReaction().copy(count = 1)
        val anotherReaction = testMessageGenerator.generateTestReaction()
        val testReactionList = listOf(myReaction, anotherReaction)
        val myMessage = testMessageGenerator.generateOwnTestMessage(testReactionList)
        val anotherMessage = testMessageGenerator.generateAnotherTestMessage(testReactionList)
        val testMessages = listOf(myMessage, anotherMessage)
        val listOfDelegateItem = testMessageGenerator.generateTestMessageDelegateItem(testMessages)

        val testNewLastEventId = "test last event id"
        val testReactionEvent = ReactionEvent(
            emojiCode = testMessages.first().reactions.first().emojiCode,
            emojiName = testMessages.first().reactions.first().emojiName,
            operation = Operation.REMOVE,
            messageId = testMessages.first().id,
            userId = testMessages.first().userId
        )
        val testReceivedReactionEventData =
            ReceivedReactionEventData(testNewLastEventId, listOf(testReactionEvent))

        val state = ChatState.Initial
        val event = ChatEvent.Internal.ReactionEventsDataReceived(testReceivedReactionEventData)
        val eventForLoadingMessage =
            ChatEvent.Internal.LoadMessagesFromNetwork(testMessages)
        // When
        val reducerWithMessages = reducer.reduce(eventForLoadingMessage, state)
        val actual = reducer.reduce(event, reducerWithMessages.state)
        // Then

        val expectedReactionList = listOf(anotherReaction)
        val expectedMyMessage = testMessageGenerator.generateOwnTestMessage(expectedReactionList)
        val expectedTestMessages = listOf(expectedMyMessage, anotherMessage)
        val expectedListOfDelegateItem = testMessageGenerator.generateTestMessageDelegateItem(expectedTestMessages)

        assertEquals(
            testNewLastEventId,
            longPollingInfoHolder.lastReactionEventId
        )
        assertEquals(
            listOfDelegateItem.size,
            (actual.state as ChatState.Content).messages.size
        )
        assertEquals(
            expectedListOfDelegateItem[1].content(),
            (actual.state as ChatState.Content).messages[1].content()
        )
    }


    @Test
    fun `reduce ReactionEventsDataReceived when (operation is add) should set reaction to (count + 1) AND update state with new reactions AND update longPollingInfoHolder`() {
        // Given
        val testMessages = testMessageGenerator.generateTestMessages()
        val listOfDelegateItem = testMessageGenerator.generateTestMessageDelegateItem()
        val testNewLastEventId = "test last event id"
        val testReactionEvent = ReactionEvent(
            emojiCode = testMessages.first().reactions.first().emojiCode,
            emojiName = testMessages.first().reactions.first().emojiName,
            operation = Operation.ADD,
            messageId = testMessages.first().id,
            userId = accountInfo.userId
        )
        val testReceivedReactionEventData =
            ReceivedReactionEventData(testNewLastEventId, listOf(testReactionEvent))

        val state = ChatState.Initial
        val event = ChatEvent.Internal.ReactionEventsDataReceived(testReceivedReactionEventData)
        val eventForLoadingMessage =
            ChatEvent.Internal.LoadMessagesFromNetwork(testMessageGenerator.generateTestMessages())
        // When
        val reducerWithMessages = reducer.reduce(eventForLoadingMessage, state)
        val actual = reducer.reduce(event, reducerWithMessages.state)
        // Then
        val actualReactionCount = ((actual.state as ChatState.Content).messages[1] as MessageDelegateItem).reactionList.first().count
        val expectedReactionCount = (listOfDelegateItem[1] as SendMessageDelegateItem).value.reactionList.first().count + 1
        assertEquals(
            testNewLastEventId,
            longPollingInfoHolder.lastReactionEventId
        )
        assertEquals(
            listOfDelegateItem.size,
            (actual.state as ChatState.Content).messages.size
        )
        assertEquals(
            expectedReactionCount,
            actualReactionCount
        )
    }

    @Test
    fun `reduce ReactionEventsDataReceived when (operation is add AND reaction was not in the message) should add new reaction AND update state with new reaction AND update longPollingInfoHolder`() {
        // Given
        val testMessages = testMessageGenerator.generateTestMessages()
        val listOfDelegateItem = testMessageGenerator.generateTestMessageDelegateItem()

        val newEmojiCode = "new emojiCode"
        val newEmojiName = "new emojiName"

        val testNewLastEventId = "test last event id"
        val testReactionEvent = ReactionEvent(
            emojiCode = newEmojiCode,
            emojiName = newEmojiName,
            operation = Operation.ADD,
            messageId = testMessages.first().id,
            userId = accountInfo.userId
        )
        val testReceivedReactionEventData =
            ReceivedReactionEventData(testNewLastEventId, listOf(testReactionEvent))

        val state = ChatState.Initial
        val event = ChatEvent.Internal.ReactionEventsDataReceived(testReceivedReactionEventData)
        val eventForLoadingMessage =
            ChatEvent.Internal.LoadMessagesFromNetwork(testMessageGenerator.generateTestMessages())
        // When
        val reducerWithMessages = reducer.reduce(eventForLoadingMessage, state)
        val actual = reducer.reduce(event, reducerWithMessages.state)
        // Then
        val myReaction = testMessageGenerator.generateMyReaction()
        val anotherReaction = testMessageGenerator.generateTestReaction()
        val newReaction = Reaction(
            accountInfo.userId,
            emojiCode = newEmojiCode,
            emojiName = newEmojiName,
            count = 1
        )
        val expectedReactionList = listOf(myReaction,anotherReaction,newReaction)
        val expectedMyMessage = testMessageGenerator.generateOwnTestMessage(expectedReactionList)
        val expectedAnotherMessage = testMessageGenerator.generateAnotherTestMessage()
        val expectedTestMessages = listOf(expectedMyMessage, expectedAnotherMessage)
        val expectedListOfDelegateItem = testMessageGenerator.generateTestMessageDelegateItem(expectedTestMessages)

        assertEquals(
            testNewLastEventId,
            longPollingInfoHolder.lastReactionEventId
        )
        assertEquals(
            listOfDelegateItem.size,
            (actual.state as ChatState.Content).messages.size
        )
        assertEquals(
            expectedListOfDelegateItem[1].content(),
            (actual.state as ChatState.Content).messages[1].content()
        )
    }

}