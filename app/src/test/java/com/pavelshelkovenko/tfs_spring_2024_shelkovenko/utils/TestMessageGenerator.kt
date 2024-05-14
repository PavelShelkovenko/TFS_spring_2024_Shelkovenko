package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Message
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Reaction
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.base.delegate_adapter.DelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.date.MessageDateTime
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.date.MessageDateTimeDelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.message.received_message.ReceivedMessageDelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.message.received_message.ReceivedMessageModel
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.message.send_message.SendMessageDelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.message.send_message.SendMessageModel

class TestMessageGenerator {


    private val testUserId = 54321

    fun generateOwnTestMessage(
        reactionList: List<Reaction> = listOf(generateMyReaction(), generateTestReaction())
    ): Message = Message(
        id = 12345,
        avatarUrl = "my avatar",
        message = "my message",
        userId = MyUserId.MY_USER_ID,
        userName = "my user name",
        dateInUTCSeconds = 12345,
        reactions = reactionList
    )

    fun generateAnotherTestMessage(
        reactionList: List<Reaction> = listOf(generateMyReaction(), generateTestReaction())
    ): Message = Message(
        id = 5345,
        avatarUrl = "avatar",
        message = "test message",
        userId = testUserId,
        userName = "test user name",
        dateInUTCSeconds = 12345,
        reactions = reactionList
    )


    fun generateTestMessageDelegateItem(
        messages: List<Message> = generateTestMessages()
    ): List<DelegateItem> {
        val dateDelegateItem = MessageDateTimeDelegateItem(
            id = generateRandomId(),
            value = MessageDateTime(
                dateTime = getFormattedDate(111)
            )
        )
        val sendMessageDelegateItem = SendMessageDelegateItem(
            id = messages.first().id,
            value = SendMessageModel(
                userId = messages.first().userId,
                textMessage = messages.first().message,
                reactionList = messages.first().reactions,
                dateInUTCSeconds = messages.first().dateInUTCSeconds
            )
        )
        val receivedMessageDelegateItem = ReceivedMessageDelegateItem(
            id = messages.last().id,
            value = ReceivedMessageModel(
                userId = messages.last().userId,
                textMessage = messages.last().message,
                reactionList = messages.last().reactions,
                dateInUTCSeconds = messages.last().dateInUTCSeconds,
                avatarUrl = messages.last().avatarUrl,
                userName = messages.last().userName
            )
        )
        return listOf(
            dateDelegateItem,
            sendMessageDelegateItem,
            receivedMessageDelegateItem
        )
    }

    fun generateTestMessages(): List<Message> = listOf(
        generateOwnTestMessage(),
        generateAnotherTestMessage()
    )

    fun generateMyReaction(): Reaction = Reaction(
        userId = MyUserId.MY_USER_ID,
        emojiCode = "my emojiCode",
        emojiName = "my emojiName",
        count = 3,
    )

    fun generateTestReaction(): Reaction = Reaction(
        userId = testUserId,
        emojiCode = "test emojiCode",
        emojiName = "test emojiName",
        count = 5,
    )
}