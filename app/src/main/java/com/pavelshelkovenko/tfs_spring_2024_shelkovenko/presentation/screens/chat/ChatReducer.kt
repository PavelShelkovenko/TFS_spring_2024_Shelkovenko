package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.AccountInfo
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Message
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Reaction
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.Operation
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.events.ReactionEvent
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.base.delegate_adapter.DelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.date.MessageDateTime
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.date.MessageDateTimeDelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.message.MessageDelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.message.received_message.ReceivedMessageDelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.message.received_message.ReceivedMessageModel
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.message.send_message.SendMessageDelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.message.send_message.SendMessageModel
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.NoAction
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.generateRandomId
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.getFormattedDate
import kotlinx.coroutines.flow.MutableStateFlow
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import javax.inject.Inject

class ChatReducer @Inject constructor(
    private val longPollingInfoHolder: LongPollingInfoHolder,
    private val paginationInfoHolder: PaginationInfoHolder,
    private val accountInfo: AccountInfo,
) : ScreenDslReducer<
        ChatEvent,
        ChatEvent.Ui,
        ChatEvent.Internal,
        ChatState,
        ChatEffect,
        ChatCommand>
    (ChatEvent.Ui::class, ChatEvent.Internal::class) {

    private var delegateItemList = MutableStateFlow<List<DelegateItem>>(emptyList())

    override fun Result.internal(event: ChatEvent.Internal) = when (event) {
        is ChatEvent.Internal.LoadMessagesFromNetwork -> {
            val messagesUi = convertMessagesToDelegateItemWithDate(event.messages)
            delegateItemList.value = messagesUi
            paginationInfoHolder.apply {
                oldestAnchor = try {
                    getFirstMessageDelegateItem().id().toString()
                } catch (e: Exception) { return@apply }
                newestAnchor = try {
                    getLastMessageDelegateItem().id().toString()
                } catch (e: Exception) { return@apply }
            }
            state { ChatState.Content(messages = messagesUi) }
        }

        is ChatEvent.Internal.LoadMessagesFromCache -> {
            if (event.messages.isEmpty()) {
                state { ChatState.Loading }
            } else {
                paginationInfoHolder.apply {
                    newestAnchor = event.messages.last().id.toString()
                }
                val messagesUi = convertMessagesToDelegateItemWithDate(event.messages)
                state { ChatState.Content(messages = messagesUi) }
            }
        }

        is ChatEvent.Internal.Error -> {
            if (delegateItemList.value.isEmpty()) {
                state { ChatState.Error(errorMessageId = event.errorMessageId) }
            } else effects { +ChatEffect.MinorError(errorMessageId = event.errorMessageId) }
        }

        is ChatEvent.Internal.MinorError -> {
            effects { +ChatEffect.MinorError(errorMessageId = event.errorMessageId) }
        }

        is ChatEvent.Internal.PagingError -> {
            if (!paginationInfoHolder.hasSentPagingError) {
                effects { +ChatEffect.MinorError(errorMessageId = event.errorMessageId) }
                paginationInfoHolder.hasSentPagingError = true
            } else NoAction
        }

        is ChatEvent.Internal.RegistrationForChatEventsDataReceived -> {

            longPollingInfoHolder.apply {
                messageQueueId = event.registrationForEventsData.messagesQueueId
                lastMessageEventId = event.registrationForEventsData.messageLastEventId
                reactionQueueId = event.registrationForEventsData.reactionsQueueId
                lastReactionEventId = event.registrationForEventsData.reactionLastEventId
            }

            commands {
                +ChatCommand.GetMessageEvents(
                    queueId = longPollingInfoHolder.messageQueueId,
                    lastEventId = longPollingInfoHolder.lastMessageEventId
                )
                +ChatCommand.GetReactionEvents(
                    queueId = longPollingInfoHolder.reactionQueueId,
                    lastEventId = longPollingInfoHolder.lastReactionEventId
                )
            }
        }

        is ChatEvent.Internal.MessageEventsDataReceived -> {
            longPollingInfoHolder.lastMessageEventId = event.receivedMessageEventData.newLastEventId
            handleReceivedMessageEvents(
                newMessages = event.receivedMessageEventData.newMessages,
                onUpdateState = { newDelegateItemsList ->
                    state {
                        ChatState.Content(messages = newDelegateItemsList)
                    }
                    effects {
                        +ChatEffect.NewMessageReceived
                    }
                }
            )
        }

        is ChatEvent.Internal.ReactionEventsDataReceived -> {
            longPollingInfoHolder.lastReactionEventId =
                event.receivedReactionEventData.newLastEventId
            handleReceivedReactionEvents(
                reactionEventList = event.receivedReactionEventData.reactionEvents,
                onUpdateState = { newDelegateItemsList ->
                    state {
                        ChatState.Content(messages = newDelegateItemsList)
                    }
                }
            )
        }

        is ChatEvent.Internal.LoadPagingNewerMessages -> {
            handlePagingNewMessages(
                messages = event.messages,
                onUpdateState = { newDelegateItemsList ->
                    state { ChatState.Content(messages = newDelegateItemsList) }
                }
            )
        }

        is ChatEvent.Internal.LoadPagingOlderMessages -> {
            handlePagingOldMessages(
                messages = event.messages,
                onUpdateState = { newDelegateItemsList ->
                    state { ChatState.Content(messages = newDelegateItemsList) }
                }
            )
        }

        ChatEvent.Internal.LoadingPagingDataFinished -> {
            paginationInfoHolder.isLoadingPagingData = false
        }

        ChatEvent.Internal.GetMessageLongPollingData -> {
            commands {
                +ChatCommand.GetMessageEvents(
                    queueId = longPollingInfoHolder.messageQueueId,
                    lastEventId = longPollingInfoHolder.lastMessageEventId
                )
            }
        }

        ChatEvent.Internal.GetReactionLongPollingData -> {
            commands {
                +ChatCommand.GetReactionEvents(
                    queueId = longPollingInfoHolder.reactionQueueId,
                    lastEventId = longPollingInfoHolder.lastReactionEventId
                )
            }
        }

        ChatEvent.Internal.CachedMessagesSaved -> {
            effects { +ChatEffect.CloseChat }
        }
    }


    override fun Result.ui(event: ChatEvent.Ui) = when (event) {
        is ChatEvent.Ui.StartProcess -> {
            commands {
                +ChatCommand.LoadMessagesFromCache(
                    streamName = event.streamName,
                    topicName = event.topicName,
                )
            }
            commands {
                +ChatCommand.LoadMessagesFromNetwork(
                    streamName = event.streamName,
                    topicName = event.topicName,
                    anchor = paginationInfoHolder.newestAnchor,
                    numAfter = COUNT_OF_MESSAGE_TO_DOWNLOAD,
                    numBefore = 2 * COUNT_OF_MESSAGE_TO_DOWNLOAD
                )
            }
        }

        is ChatEvent.Ui.ReloadData -> {
            state { ChatState.Loading }
            commands {
                +ChatCommand.LoadMessagesFromNetwork(
                    streamName = event.streamName,
                    topicName = event.topicName,
                    anchor = paginationInfoHolder.newestAnchor,
                    numAfter = COUNT_OF_MESSAGE_TO_DOWNLOAD,
                    numBefore = 2 * COUNT_OF_MESSAGE_TO_DOWNLOAD
                )
            }
        }

        is ChatEvent.Ui.LoadPagingNewerMessages -> {
            if (paginationInfoHolder.isLoadingPagingData || paginationInfoHolder.hasLoadedNewestMessage) {
                NoAction
            } else {
                commands {
                    +ChatCommand.LoadPagingNewerMessages(
                        streamName = event.streamName,
                        topicName = event.topicName,
                        anchor = paginationInfoHolder.newestAnchor,
                        numAfter = COUNT_OF_MESSAGE_TO_DOWNLOAD,
                        numBefore = 0,
                    )
                }
                paginationInfoHolder.isLoadingPagingData = true
            }
        }

        is ChatEvent.Ui.LoadPagingOlderMessages -> {
            if (paginationInfoHolder.isLoadingPagingData || paginationInfoHolder.hasLoadedOldestMessage) {
                NoAction
            } else {
                commands {
                    +ChatCommand.LoadPagingOlderMessages(
                        streamName = event.streamName,
                        topicName = event.topicName,
                        anchor = paginationInfoHolder.oldestAnchor,
                        numAfter = 0,
                        numBefore = COUNT_OF_MESSAGE_TO_DOWNLOAD,
                    )
                }
                paginationInfoHolder.isLoadingPagingData = true
            }
        }

        is ChatEvent.Ui.RemoveReaction -> {
            commands {
                +ChatCommand.RemoveReaction(
                    messageId = event.messageId,
                    emojiName = event.emojiName,
                    emojiCode = event.emojiCode
                )
            }
        }

        is ChatEvent.Ui.SendMessage -> {
            commands {
                +ChatCommand.SendMessage(
                    message = event.message,
                    topicName = event.topicName,
                    streamName = event.streamName
                )
            }
        }

        is ChatEvent.Ui.SendReaction -> {
            commands {
                +ChatCommand.SendReaction(
                    messageId = event.messageId,
                    emojiName = event.emojiName,
                    emojiCode = event.emojiCode
                )
            }
        }

        is ChatEvent.Ui.RegisterForChatEvents -> {
            commands {
                +ChatCommand.RegisterForChatEvents(
                    streamName = event.streamName,
                    topicName = event.topicName
                )
            }
        }

        is ChatEvent.Ui.OnEmojiClick -> {
            handleEmojiClick(
                messageId = event.messageId,
                emojiCode = event.emojiCode,
                onRemoveReaction = {
                    commands {
                        +ChatCommand.RemoveReaction(
                            messageId = event.messageId,
                            emojiCode = event.emojiCode,
                            emojiName = event.emojiName
                        )
                    }
                },
                onSendReaction = {
                    commands {
                        +ChatCommand.SendReaction(
                            messageId = event.messageId,
                            emojiCode = event.emojiCode,
                            emojiName = event.emojiName
                        )
                    }
                }
            )
        }

        is ChatEvent.Ui.ClosingChat -> {
            commands {
                +ChatCommand.SaveMessagesInCache(
                    streamName = event.streamName,
                    topicName = event.topicName,
                    messages = getMessagesForCaching()
                )
            }
        }
    }

    private fun handleEmojiClick(
        messageId: Int,
        emojiCode: String,
        onRemoveReaction: () -> Unit,
        onSendReaction: () -> Unit,
    ) {
        val message = delegateItemList.value.find { it.id() == messageId }
        if (message != null) {
            val reactionWithSimilarEmojiCode =
                (message as MessageDelegateItem).reactionList.find { reaction ->
                    reaction.emojiCode == emojiCode
                }
            reactionWithSimilarEmojiCode?.let { reaction ->
                if (reaction.userId == accountInfo.userId) {
                    onRemoveReaction()
                } else {
                    onSendReaction()
                }
            }
        }
    }

    private fun getMessagesForCaching(): List<Message> {
        val messagesUi = delegateItemList.value.filterIsInstance<MessageDelegateItem>().takeLast(LIMIT_OF_MESSAGES_FOR_CACHING)
        val messages = messagesUi.map { messageDelegateItem ->
            when(messageDelegateItem) {
                is ReceivedMessageDelegateItem -> {
                    Message(
                        id = messageDelegateItem.id,
                        userId = messageDelegateItem.value.userId,
                        avatarUrl = messageDelegateItem.value.avatarUrl,
                        message = messageDelegateItem.value.textMessage,
                        userName = messageDelegateItem.value.userName,
                        dateInUTCSeconds = messageDelegateItem.value.dateInUTCSeconds,
                        reactions = messageDelegateItem.value.reactionList,
                    )
                }
                is SendMessageDelegateItem -> {
                    Message(
                        id = messageDelegateItem.id,
                        userId = messageDelegateItem.value.userId,
                        avatarUrl = "",
                        message = messageDelegateItem.value.textMessage,
                        userName = "",
                        dateInUTCSeconds = messageDelegateItem.value.dateInUTCSeconds,
                        reactions = messageDelegateItem.value.reactionList,
                    )
                }
                else -> {
                    throw IllegalStateException("MessageDelegateItem should be either ReceivedMessageDelegateItem or SendMessageDelegateItem")
                }
            }
        }
        return messages
    }

    private fun handlePagingNewMessages(
        messages: List<Message>,
        onUpdateState: (List<DelegateItem>) -> Unit,
    ) {
        val messagesUi = convertMessagesToDelegateItemWithDate(messages)
        try {
            if (messagesUi.last().id() == getLastMessageDelegateItem().id()) {
                paginationInfoHolder.hasLoadedNewestMessage = true
            } else {
                val listWithoutDuplicate = messagesUi.drop(1)
                val newDelegateList = delegateItemList.value + listWithoutDuplicate
                delegateItemList.value = newDelegateList
                paginationInfoHolder.newestAnchor = getLastMessageDelegateItem().id().toString()
                onUpdateState(newDelegateList)
            }
        } catch (e: Exception) {
            delegateItemList.value = messagesUi
            paginationInfoHolder.newestAnchor = getLastMessageDelegateItem().id().toString()
            onUpdateState(messagesUi)
        }
    }

    private fun handlePagingOldMessages(
        messages: List<Message>,
        onUpdateState: (List<DelegateItem>) -> Unit,
    ) {
        val messagesUi = convertMessagesToDelegateItemWithDate(
            messages = messages,
        )
        try {
            if (messagesUi.first().id() == getFirstMessageDelegateItem().id()) {
                paginationInfoHolder.hasLoadedOldestMessage = true
            } else {
                val listWithoutDuplicate = messagesUi.dropLast(1)
                val newDelegateList = listWithoutDuplicate + delegateItemList.value
                delegateItemList.value = newDelegateList
                paginationInfoHolder.oldestAnchor = getFirstMessageDelegateItem().id().toString()
                onUpdateState(newDelegateList)
            }
        } catch (e: Exception) {
            delegateItemList.value = messagesUi
            paginationInfoHolder.oldestAnchor = getFirstMessageDelegateItem().id().toString()
            onUpdateState(messagesUi)
        }
    }

    private fun convertMessagesToDelegateItemWithDate(messages: List<Message>): List<DelegateItem> {
        return (messages)
            .sortedBy { it.dateInUTCSeconds }
            .groupBy { message ->
                getFormattedDate(message.dateInUTCSeconds)
            }
            .flatMap { (date, messages) ->
                if (alreadyExistingDateInChat(date = date)) {
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
            if (delegateItem is MessageDateTimeDelegateItem && delegateItem.value.dateTime == date) {
                return true
            }
        }
        return false
    }

    private fun parseMessageToDelegateItem(messages: List<Message>): List<DelegateItem> {
        val delegateMessagesList = mutableListOf<DelegateItem>()
        messages.forEach { message ->
            if (message.userId == accountInfo.userId) {
                val delegateMessage = SendMessageDelegateItem(
                    id = message.id,
                    value = SendMessageModel(
                        userId = message.userId,
                        textMessage = message.message,
                        reactionList = message.reactions,
                        dateInUTCSeconds = message.dateInUTCSeconds
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
                        userName = message.userName,
                        dateInUTCSeconds = message.dateInUTCSeconds
                    )
                )
                delegateMessagesList.add(delegateMessage)
            }
        }
        return delegateMessagesList
    }

    private fun getFirstMessageDelegateItem(): MessageDelegateItem {
        return delegateItemList.value.first { delegateItem ->
            delegateItem is MessageDelegateItem
        } as MessageDelegateItem
    }

    private fun getLastMessageDelegateItem(): MessageDelegateItem {
        return delegateItemList.value.last { delegateItem ->
            delegateItem is MessageDelegateItem
        } as MessageDelegateItem
    }

    private fun handleReceivedMessageEvents(
        newMessages: List<Message>,
        onUpdateState: (List<DelegateItem>) -> Unit
    ) {
        val messagesUi = convertMessagesToDelegateItemWithDate(messages = newMessages)
        val oldMessagesUi = delegateItemList.value
        val newMessagesUi = oldMessagesUi + messagesUi
        delegateItemList.value = newMessagesUi
        onUpdateState(newMessagesUi)
    }

    private fun handleReceivedReactionEvents(
        reactionEventList: List<ReactionEvent>,
        onUpdateState: (List<DelegateItem>) -> Unit
    ) {
        reactionEventList.forEach { reactionEvent ->
            val message = delegateItemList.value.find { it.id() == reactionEvent.messageId }
                ?: return
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
                        emojiName = reactionEvent.emojiName,
                        onUpdateState = onUpdateState
                    )
                }

                Operation.REMOVE -> {
                    handleRemoveReaction(
                        messageWhereRemoveReaction = oldMessage,
                        messageIndex = messageIndex,
                        reactionForRemoving = reactionWithSimilarEmojiCode,
                        userId = reactionEvent.userId,
                        onUpdateState = onUpdateState
                    )
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
        onUpdateState: (List<DelegateItem>) -> Unit
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
                newReactionList = newReactionList,
                onUpdateState = onUpdateState
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
                newReactionList = newReactionList,
                onUpdateState = onUpdateState
            )
        }
    }

    private fun handleRemoveReaction(
        messageWhereRemoveReaction: MessageDelegateItem,
        messageIndex: Int,
        reactionForRemoving: Reaction?,
        userId: Int,
        onUpdateState: (List<DelegateItem>) -> Unit,
    ) {
        val mutableReactionList = messageWhereRemoveReaction.reactionList.toMutableList()
        if (reactionForRemoving != null) {
            if (reactionForRemoving.count == 1) {
                mutableReactionList.remove(reactionForRemoving)
                setNewMessageAndUpdateState(
                    oldMessage = messageWhereRemoveReaction,
                    messageIndex = messageIndex,
                    newReactionList = mutableReactionList,
                    onUpdateState = onUpdateState
                )
            } else {
                if (userId == accountInfo.userId) {
                    removeMyReactionFromReactionList(
                        messageWhereRemoveReaction = messageWhereRemoveReaction,
                        messageIndex = messageIndex,
                        reactionList = mutableReactionList,
                        reactionForRemove = reactionForRemoving,
                        onUpdateState = onUpdateState
                    )
                } else {
                    removeReactionFromReactionList(
                        messageWhereRemoveReaction = messageWhereRemoveReaction,
                        messageIndex = messageIndex,
                        reactionList = mutableReactionList,
                        userId = userId,
                        reaction = reactionForRemoving,
                        onUpdateState = onUpdateState
                    )
                }
            }
        }
    }

    private fun changeReaction(
        newReaction: Reaction,
        oldReaction: Reaction,
        reactionList: MutableList<Reaction>
    ) {
        val oldReactionIndex = reactionList.indexOf(oldReaction)
        reactionList[oldReactionIndex] = newReaction
    }

    private fun removeMyReactionFromReactionList(
        messageWhereRemoveReaction: MessageDelegateItem,
        messageIndex: Int,
        reactionList: MutableList<Reaction>,
        reactionForRemove: Reaction,
        onUpdateState: (List<DelegateItem>) -> Unit
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
            newReactionList = reactionList,
            onUpdateState = onUpdateState
        )
    }

    private fun removeReactionFromReactionList(
        messageWhereRemoveReaction: MessageDelegateItem,
        messageIndex: Int,
        reactionList: MutableList<Reaction>,
        userId: Int,
        reaction: Reaction,
        onUpdateState: (List<DelegateItem>) -> Unit
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
            newReactionList = reactionList,
            onUpdateState = onUpdateState
        )
    }

    private fun setNewMessageAndUpdateState(
        oldMessage: MessageDelegateItem,
        messageIndex: Int,
        newReactionList: MutableList<Reaction>,
        onUpdateState: (List<DelegateItem>) -> Unit
    ) {
        val newMessage = oldMessage.copy()
        newMessage.changeReactionList(newReactionList)
        val newList = delegateItemList.value.toMutableList().apply {
            set(messageIndex, newMessage)
        }
        delegateItemList.value = newList
        onUpdateState(newList)
    }

    companion object {
        private const val COUNT_OF_MESSAGE_TO_DOWNLOAD = 25
        private const val LIMIT_OF_MESSAGES_FOR_CACHING = 50
    }
}