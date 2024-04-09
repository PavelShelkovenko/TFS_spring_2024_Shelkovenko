package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat

import android.app.Application
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.AndroidViewModel
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.delegate_adapter.DelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.models.User
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.models.UserOnlineStatus
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.date.MessageDateTime
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.date.MessageDateTimeDelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message.MessageDelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message.reaction.EmojiFactory
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message.reaction.Reaction
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message.received_message.ReceivedMessageDelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message.received_message.ReceivedMessageModel
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message.send_message.SendMessageDelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message.send_message.SendMessageModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

class ChatViewModel(
    application: Application
) : AndroidViewModel(application) {

    // Унаследовал от AndroidViewModel() на время, эта вьюмодель только для
    // этой домашки чтобы получить доступ к drawable. Аватарка будет хранится в будущем не в папке res

    private val _stubMessages = MutableStateFlow<List<DelegateItem>>(emptyList())
    val stubMessages: StateFlow<List<DelegateItem>>
        get() = _stubMessages


    private val _messageToSend = MutableStateFlow("")

    private val localUserAvatar = ResourcesCompat.getDrawable(
        application.resources,
        R.drawable.ic_launcher_foreground,
        null
    )?.toBitmap() ?: throw IllegalArgumentException("Drawable not found")

    private val testUserAvatar = ResourcesCompat.getDrawable(
        application.resources,
        R.drawable.ic_launcher_background,
        null
    )?.toBitmap() ?: throw IllegalArgumentException("Drawable not found")

    val localUser = User(
        id = Random.nextInt(0, 1000000),
        avatar = localUserAvatar,
        name = "Pavel Shelkovenko",
        email = "",
        onlineStatus = UserOnlineStatus.ACTIVE,
        activityStatus = "In a meeting"
    )

    private val testUser = User(
        id = Random.nextInt(0, 1000000),
        avatar = testUserAvatar,
        name = "Ilya Shelkovenko",
        email = "",
        onlineStatus = UserOnlineStatus.ACTIVE,
        activityStatus = "In a meeting"
    )

    private val stubReaction
        get() = Reaction(
            userId = listOf(localUser.id, testUser.id).shuffled().first(),
            emojiCode = EmojiFactory.getEmojiList().shuffled().first().code,
            count = Random.nextInt(1, 2)
        )

    private val stubReactionList = listOf(
        stubReaction,
        stubReaction,
        stubReaction,
    )

    private val stabMessage1
        get() = SendMessageDelegateItem(
            id = Random.nextInt(0, 1000000),
            value = SendMessageModel(
                userId = localUser.id,
                textMessage = "Some Text Message!",
                reactionList = stubReactionList
            )
        )
    private val stabMessage2
        get() = ReceivedMessageDelegateItem(
            id = Random.nextInt(0, 1000000),
            value = ReceivedMessageModel(
                userId = testUser.id,
                userName = testUser.name,
                userAvatar = testUser.avatar,
                textMessage = "Another test message!",
                reactionList = stubReactionList
            )
        )

    init {
        setupList()
    }

    private fun setupList() {

        val dateInfo = MessageDateTimeDelegateItem(
            id = 1,
            value = MessageDateTime("1 February")
        )
        _stubMessages.value = listOf(
            stabMessage2,
            stabMessage1,
            dateInfo,
            stabMessage1,
            dateInfo,
            stabMessage2,
            dateInfo,
            stabMessage1,
            stabMessage1,
            stabMessage2,
            stabMessage1,
            dateInfo,
            stabMessage1,
            stabMessage2,
        )
    }

    fun addMessage() {
        val newMessage = SendMessageDelegateItem(
            id = Random.nextInt(0, 1000000),
            value = SendMessageModel(
                userId = localUser.id,
                textMessage = _messageToSend.value,
                reactionList = emptyList()
            )
        )
        _stubMessages.value = _stubMessages.value.toMutableList().apply {
            add(newMessage)
        }
    }

    fun processMessageFieldChanges(newMessage: String) {
        _messageToSend.value = newMessage
    }

    fun addEmoji(messageId: Int, emojiCode: String) {
        var isReactionOld = true
        val (message, messageIndex) = try {
            findMessageById(messageId)
        } catch (ex: Exception) {
            return
        }
        val reaction = try {
            findReactionByCode(messageId, emojiCode)
        } catch (ex: Exception) {
            isReactionOld = false
            Reaction(localUser.id, emojiCode, 1)
        }
        val newReactionList = message.reactionList.toMutableList()
        if (isReactionOld) {
            if (reaction.userId == localUser.id) {
                return
            }
            val newReaction = reaction.copy(userId = localUser.id, count = reaction.count + 1)
            changeReaction(
                newReaction = newReaction,
                oldReaction = reaction,
                reactionList = newReactionList
            )
        } else {
            newReactionList.add(reaction)
        }
        setNewMessage(message, messageIndex, newReactionList)
    }

    fun changeEmojiStatus(messageId: Int, emojiCode: String) {
        val (message, messageIndex) = try {
            findMessageById(messageId)
        } catch (ex: Exception) {
            return
        }
        val oldReaction = try {
            findReactionByCode(messageId, emojiCode)
        } catch (ex: Exception) {
            return
        }
        val newReactionList = message.reactionList.toMutableList()
        if (oldReaction.userId == localUser.id) {
            if (oldReaction.count == 1) {
                newReactionList.remove(oldReaction)
                setNewMessage(message, messageIndex, newReactionList)
            } else {
                val newReaction =
                    oldReaction.copy(userId = testUser.id, count = oldReaction.count - 1)
                changeReaction(
                    newReaction = newReaction,
                    oldReaction,
                    reactionList = newReactionList
                )
                setNewMessage(message, messageIndex, newReactionList)
            }
        } else {
            val newReaction = oldReaction.copy(userId = localUser.id, count = oldReaction.count + 1)
            changeReaction(newReaction = newReaction, oldReaction, reactionList = newReactionList)
            setNewMessage(message, messageIndex, newReactionList)
        }
    }

    private fun setNewMessage(
        message: MessageDelegateItem,
        messageIndex: Int,
        newReactionList: MutableList<Reaction>
    ) {
        val newMessage = message.copy()
        newMessage.changeReactionList(newReactionList)
        val newList = _stubMessages.value.toMutableList().apply {
            set(messageIndex, newMessage)
        }
        _stubMessages.value = newList
    }

    private fun changeReaction(
        newReaction: Reaction,
        oldReaction: Reaction,
        reactionList: MutableList<Reaction>
    ) {
        val oldReactionIndex = reactionList.indexOf(oldReaction)
        reactionList[oldReactionIndex] = newReaction
    }

    private fun findReactionByCode(messageId: Int, emojiCode: String): Reaction {
        val (message, _) = findMessageById(messageId)
        val reaction = message.reactionList.find { reaction ->
            reaction.emojiCode == emojiCode
        } ?: throw IllegalStateException("No such reaction with given emoji code")
        return reaction
    }

    private fun findMessageById(messageId: Int): Pair<MessageDelegateItem, Int> {
        val message = _stubMessages.value.filterIsInstance<MessageDelegateItem>().find {
            it.id == messageId
        } ?: throw IllegalStateException("No such message with given message id")
        val messageIndex = _stubMessages.value.indexOf(message)
        return Pair(message, messageIndex)
    }

}