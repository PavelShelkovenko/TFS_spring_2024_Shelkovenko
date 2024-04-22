package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.utils.MyUserId
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.delegate_adapter.DelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.ChatRepository
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Message
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Reaction
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.Operation
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.ReactionEvent
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.generateRandomId
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.getFormattedDate
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.date.MessageDateTime
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.date.MessageDateTimeDelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message.MessageDelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message.received_message.ReceivedMessageDelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message.received_message.ReceivedMessageModel
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message.send_message.SendMessageDelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message.send_message.SendMessageModel
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.runCatchingNonCancellation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel(
    private val repository: ChatRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow<ChatScreenState>(ChatScreenState.Initial)
    val screenState = _screenState.asStateFlow()


    private val messageToSend = MutableStateFlow("")
    private var delegateItemList = MutableStateFlow<List<DelegateItem>>(emptyList())
    private val _isLoadingNextMessages = MutableStateFlow(false)
    val isLoadingNextMessages = _isLoadingNextMessages

    private var messageQueueId: String = ""
    private var hasLoadedLastMessage = false
    private var hasLoadedFirstMessage = false
    private var reactionQueueId: String = ""
    private var lastMessageEventId: String = ""
    private var lastReactionEventId: String = ""
    private var newestAnchor = "0"
    private var oldestAnchor = "0"

    fun registerForEvents(streamName: String, topicName: String) {
        viewModelScope.launch {
            runCatchingNonCancellation {
                repository.registerForEvents(
                    streamName = streamName,
                    topicName = topicName
                )
            }.onSuccess { registrationForEventsData ->
                messageQueueId = registrationForEventsData.messagesQueueId
                lastMessageEventId = registrationForEventsData.messageLastEventId
                reactionQueueId = registrationForEventsData.reactionsQueueId
                lastReactionEventId = registrationForEventsData.reactionLastEventId
                getMessageEvent()
                getReactionEvent()
            }.onFailure { error ->
                //_screenState.value = ChatScreenState.Error(it.message.toString())
                Log.e("TAG", error.message.toString())
            }
        }
    }


    fun sendMessage(streamName: String, topicName: String) {
        viewModelScope.launch {
            runCatchingNonCancellation {
                repository.sendMessage(
                    streamName = streamName,
                    topicName = topicName,
                    message = messageToSend.value
                )
            }.onFailure { error ->
                //_screenState.value = (ChatScreenState.Error(error.message.toString()))
                Log.e("TAG", error.message.toString())
            }
        }
    }

    fun sendReaction(messageId: Int, emojiCode: String, emojiName: String) {
        viewModelScope.launch {
            runCatchingNonCancellation {
                repository.sendReaction(
                    messageId = messageId,
                    emojiName = emojiName,
                    emojiCode = emojiCode
                )
            }.onFailure { error ->
               // _screenState.value = (ChatScreenState.Error(error.message.toString()))
                Log.e("TAG", error.message.toString())
            }
        }
    }

    private fun removeReaction(messageId: Int, emojiCode: String, emojiName: String) {
        viewModelScope.launch {
            runCatchingNonCancellation {
                repository.removeReaction(
                    messageId = messageId,
                    emojiName = emojiName,
                    emojiCode = emojiCode
                )
            }.onFailure { error ->
                //_screenState.value = (ChatScreenState.Error(error.message.toString()))
                Log.e("TAG", error.message.toString())
            }
        }
    }


    fun processMessageFieldChanges(newMessage: String) {
        messageToSend.value = newMessage
    }

    fun changeEmojiStatus(messageId: Int, emojiCode: String, emojiName: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val message = delegateItemList.value.find { it.id() == messageId }
                    ?: return@withContext
                val reactionWithSimilarEmojiCode =
                    (message as MessageDelegateItem).reactionList.find { reaction ->
                        reaction.emojiCode == emojiCode
                    }
                reactionWithSimilarEmojiCode?.let { reaction ->
                    if (reaction.userId == MyUserId.MY_USER_ID) {
                        removeReaction(
                            messageId = messageId,
                            emojiCode = emojiCode,
                            emojiName = emojiName
                        )
                    } else {
                        sendReaction(
                            messageId = messageId,
                            emojiCode = emojiCode,
                            emojiName = emojiName
                        )
                    }
                }
            }
        }
    }


    fun load60messages(streamName: String, topicName: String) {
        viewModelScope.launch {
            _screenState.value = ChatScreenState.Loading
            runCatchingNonCancellation {
                val messages = repository.getMessages(
                    streamName = streamName,
                    topicName = topicName,
                    anchor = "first_unread",
                    numAfter = 20,
                    numBefore = 40,
                )
                withContext(Dispatchers.Default) {
                    val messagesUi = convertMessagesToDelegateItemWithDate(messages)
                    delegateItemList.value = messagesUi
                    oldestAnchor = findFirstMessageDelegateItem().id().toString()
                    newestAnchor = findLastMessageDelegateItem().id().toString()
                    _screenState.value = ChatScreenState.Content(messages = messagesUi)
                }
            }.onFailure {
                _screenState.value = ChatScreenState.Error(it.message.toString())
            }
        }
    }

    private fun findFirstMessageDelegateItem(): MessageDelegateItem {
        return delegateItemList.value.first { it is MessageDelegateItem } as MessageDelegateItem
    }

    private fun findLastMessageDelegateItem(): MessageDelegateItem {
        return delegateItemList.value.last { it is MessageDelegateItem } as MessageDelegateItem
    }

    fun load20NewestMessages(streamName: String, topicName: String) {
        if (!hasLoadedLastMessage) {
            viewModelScope.launch {
                _isLoadingNextMessages.value = true
                withContext(Dispatchers.Default) {
                    runCatchingNonCancellation {
                        repository.getMessages(
                            streamName = streamName,
                            topicName = topicName,
                            anchor = newestAnchor,
                            numAfter = 20,
                            numBefore = 0,
                        )
                    }.onSuccess { messages ->
                        val messagesUi = convertMessagesToDelegateItemWithDate(messages)
                        if (messagesUi.first().id() == findLastMessageDelegateItem().id()) {
                            _isLoadingNextMessages.value = false
                            hasLoadedLastMessage = true
                            return@withContext
                        }
                        val newDelegateList = delegateItemList.value + messagesUi
                        delegateItemList.value = newDelegateList
                        newestAnchor = findLastMessageDelegateItem().id().toString()
                        _screenState.value = ChatScreenState.Content(messages = newDelegateList)
                        _isLoadingNextMessages.value = false
                    }.onFailure { error ->
                        Log.d("TAG", error.message.toString())
                        _isLoadingNextMessages.value = false
                    }
                }
            }
        }
    }

    fun load20OldestMessages(streamName: String, topicName: String) {
        if (!hasLoadedFirstMessage) {
            viewModelScope.launch {
                _isLoadingNextMessages.value = true
                withContext(Dispatchers.Default) {
                    runCatchingNonCancellation {
                        repository.getMessages(
                            streamName = streamName,
                            topicName = topicName,
                            anchor = oldestAnchor,
                            numAfter = 0,
                            numBefore = 20,
                        )
                    }.onSuccess { messages ->
                        val messagesUi = convertMessagesToDelegateItemWithDate(messages)
                        /*
                         Тут нужно проверка чтобы не вставлял дупликаты, из-за того, что getMessages
                         возвращает список сообщений вместе с якорем
                         */
                        if (messagesUi.first().id() == findFirstMessageDelegateItem().id()) {
                            _isLoadingNextMessages.value = false
                            hasLoadedFirstMessage = true
                            return@withContext
                        }
                        /*
                         Так как getMessages(anchor = anchor) возвращает список сообщений вместе с сообщением которое было
                         взято в качестве якоря, то нужно его дропнуть чтобы не было дупликатов
                         */
                        val listWithoutDuplicate = messagesUi.dropLast(1)
                        val newDelegateList = listWithoutDuplicate + delegateItemList.value
                        delegateItemList.value = newDelegateList
                        oldestAnchor = findFirstMessageDelegateItem().id().toString()
                        _screenState.value = ChatScreenState.Content(messages = newDelegateList)
                        _isLoadingNextMessages.value = false
                    }.onFailure { error ->
                        Log.d("TAG", error.message.toString())
                        _isLoadingNextMessages.value = false
                    }
                }
            }
        }

    }


    private fun getReactionEvent() {
        viewModelScope.launch {
            while (true) {
                runCatchingNonCancellation {
                    repository.getReactionEvents(
                        queueId = reactionQueueId,
                        lastEventId = lastReactionEventId
                    )
                }.onSuccess { receivedReactionEventData ->
                    lastReactionEventId = receivedReactionEventData.newLastEventId
                    handleReceivedReactionEvents(receivedReactionEventData.reactionEvents)
                }.onFailure {
                    Log.d("TAG", it.message.toString())
                }
                delay(3000L)
            }
        }
    }

    private fun getMessageEvent() {
        viewModelScope.launch {
            while (true) {
                runCatchingNonCancellation {
                    repository.getMessageEvents(
                        queueId = messageQueueId,
                        lastEventId = lastMessageEventId
                    )
                }.onSuccess { receivedMessageEventData ->
                    lastMessageEventId = receivedMessageEventData.newLastEventId
                    handleReceivedMessageEvents(receivedMessageEventData.newMessages)
                }.onFailure {
                    Log.d("TAG", it.message.toString())
                }
                delay(3000L)
            }
        }
    }

    private suspend fun handleReceivedMessageEvents(newMessages: List<Message>) {
        withContext(Dispatchers.Default) {
            val messagesUi = parseMessageToDelegateItem(messages = newMessages)
            val oldMessagesUi = delegateItemList.value
            val newMessagesUi = oldMessagesUi + messagesUi
            delegateItemList.value = newMessagesUi
            _screenState.value = ChatScreenState.Content(messages = newMessagesUi)
        }
    }

    private suspend fun handleReceivedReactionEvents(reactionEventList: List<ReactionEvent>) {
        withContext(Dispatchers.Default) {
            reactionEventList.forEach { reactionEvent ->
                val message = delegateItemList.value.find { it.id() == reactionEvent.messageId }
                    ?: return@withContext
                val (oldMessage, messageIndex) =
                    (message as MessageDelegateItem) to delegateItemList.value.indexOf(message)
                val reactionWithSimilarEmojiCode = oldMessage.reactionList.find { reaction ->
                    reaction.emojiCode == reactionEvent.emojiCode
                }
                when (reactionEvent.operation) {
                    Operation.ADD -> {
                        handleAddReaction(
                            messageWhereAddReaction = oldMessage,
                            messageIndex = messageIndex,
                            reactionForAdding = reactionWithSimilarEmojiCode,
                            userId = reactionEvent.userId,
                            emojiCode = reactionEvent.emojiCode,
                            emojiName = reactionEvent.emojiName
                        )
                    }

                    Operation.REMOVE -> {
                        handleRemoveReaction(
                            messageWhereRemoveReaction = oldMessage,
                            messageIndex = messageIndex,
                            reactionForRemoving = reactionWithSimilarEmojiCode,
                            userId = reactionEvent.userId
                        )
                    }
                }
            }
        }
    }

    private fun handleAddReaction(
        messageWhereAddReaction: MessageDelegateItem,
        messageIndex: Int,
        reactionForAdding: Reaction?,
        userId: Int,
        emojiCode: String,
        emojiName: String,
    ) {
        if (reactionForAdding == null) {
            val newReaction = Reaction(
                userId = userId,
                emojiCode = emojiCode,
                emojiName = emojiName,
                count = 1
            )
            val newReactionList = messageWhereAddReaction.reactionList.toMutableList()
            newReactionList.add(newReaction)
            setNewMessageAndUpdateState(
                oldMessage = messageWhereAddReaction,
                messageIndex = messageIndex,
                newReactionList = newReactionList
            )
        } else {
            val newReaction = reactionForAdding.copy(
                userId = userId,
                count = reactionForAdding.count + 1
            )
            val newReactionList = messageWhereAddReaction.reactionList.toMutableList()
            changeReaction(
                newReaction = newReaction,
                oldReaction = reactionForAdding,
                reactionList = newReactionList
            )
            setNewMessageAndUpdateState(
                oldMessage = messageWhereAddReaction,
                messageIndex = messageIndex,
                newReactionList = newReactionList
            )
        }
    }

    private fun handleRemoveReaction(
        messageWhereRemoveReaction: MessageDelegateItem,
        messageIndex: Int,
        reactionForRemoving: Reaction?,
        userId: Int,
    ) {
        val mutableReactionList = messageWhereRemoveReaction.reactionList.toMutableList()
        if (reactionForRemoving != null) {
            if (reactionForRemoving.count == 1) {
                mutableReactionList.remove(reactionForRemoving)
                setNewMessageAndUpdateState(
                    oldMessage = messageWhereRemoveReaction,
                    messageIndex = messageIndex,
                    newReactionList = mutableReactionList
                )
            } else {
                if (userId == MyUserId.MY_USER_ID) {
                    removeMyReactionFromReactionList(
                        messageWhereRemoveReaction = messageWhereRemoveReaction,
                        messageIndex = messageIndex,
                        reactionList = mutableReactionList,
                        reactionForRemove = reactionForRemoving
                    )
                } else {
                    removeReactionFromReactionList(
                        messageWhereRemoveReaction = messageWhereRemoveReaction,
                        messageIndex = messageIndex,
                        reactionList = mutableReactionList,
                        userId = userId,
                        reaction = reactionForRemoving
                    )
                }
            }
        }
    }

    private fun convertMessagesToDelegateItemWithDate(messages: List<Message>): List<DelegateItem> {
        return (messages)
            .sortedBy { it.dateInUTCSeconds }
            .groupBy { message ->
                getFormattedDate(message.dateInUTCSeconds)
            }
            .flatMap { (date, messages) ->
                if (alreadyExistingDateInChat(date)) {
                    parseMessageToDelegateItem(messages)
                } else {
                    listOf(
                        MessageDateTimeDelegateItem(
                            id = generateRandomId(),
                            value = MessageDateTime(
                                dateTime = date
                            )
                        )
                    ) + parseMessageToDelegateItem(
                        messages
                    )
                }

            }
    }

    private fun alreadyExistingDateInChat(date: String): Boolean {
        delegateItemList.value.forEach { delegateItem ->
            if (delegateItem is MessageDateTimeDelegateItem) {
                if (delegateItem.value.dateTime == date) {
                    return true
                }
            }
        }
        return false
    }

    private fun removeMyReactionFromReactionList(
        messageWhereRemoveReaction: MessageDelegateItem,
        messageIndex: Int,
        reactionList: MutableList<Reaction>,
        reactionForRemove: Reaction
    ) {
        val newReaction = reactionForRemove.copy(
            userId = generateRandomId(),
            count = reactionForRemove.count - 1
        )
        changeReaction(
            newReaction = newReaction,
            oldReaction = reactionForRemove,
            reactionList = reactionList
        )
        setNewMessageAndUpdateState(
            oldMessage = messageWhereRemoveReaction,
            messageIndex = messageIndex,
            newReactionList = reactionList
        )
    }

    private fun removeReactionFromReactionList(
        messageWhereRemoveReaction: MessageDelegateItem,
        messageIndex: Int,
        reactionList: MutableList<Reaction>,
        userId: Int,
        reaction: Reaction
    ) {
        val newReaction = reaction.copy(
            userId = userId,
            count = reaction.count - 1
        )
        changeReaction(
            newReaction = newReaction,
            oldReaction = reaction,
            reactionList = reactionList
        )
        setNewMessageAndUpdateState(
            oldMessage = messageWhereRemoveReaction,
            messageIndex = messageIndex,
            newReactionList = reactionList
        )
    }

    private fun changeReaction(
        newReaction: Reaction,
        oldReaction: Reaction,
        reactionList: MutableList<Reaction>
    ) {
        val oldReactionIndex = reactionList.indexOf(oldReaction)
        reactionList[oldReactionIndex] = newReaction
    }

    private fun setNewMessageAndUpdateState(
        oldMessage: MessageDelegateItem,
        messageIndex: Int,
        newReactionList: MutableList<Reaction>
    ) {
        val newMessage = oldMessage.copy()
        newMessage.changeReactionList(newReactionList)
        val newList = delegateItemList.value.toMutableList().apply {
            set(messageIndex, newMessage)
        }
        delegateItemList.value = newList
        _screenState.value = ChatScreenState.Content(messages = delegateItemList.value)
    }

    private fun parseMessageToDelegateItem(messages: List<Message>): List<DelegateItem> {
        val delegateMessagesList = mutableListOf<DelegateItem>()
        messages.forEach { message ->
            if (message.userId == MyUserId.MY_USER_ID) {
                val delegateMessage = SendMessageDelegateItem(
                    id = message.id,
                    value = SendMessageModel(
                        userId = message.userId,
                        textMessage = message.message,
                        reactionList = message.reactions
                    )
                )
                delegateMessagesList.add(delegateMessage)
            } else {
                val delegateMessage = ReceivedMessageDelegateItem(
                    id = message.id,
                    value = ReceivedMessageModel(
                        userId = message.userId,
                        avatarUrl = message.avatarUrl,
                        textMessage = message.message,
                        reactionList = message.reactions,
                        userName = message.userName
                    )
                )
                delegateMessagesList.add(delegateMessage)
            }
        }
        return delegateMessagesList
    }
}