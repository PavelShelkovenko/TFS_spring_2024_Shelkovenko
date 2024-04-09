package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message.received_message

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.delegate_adapter.DelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message.MessageDelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message.reaction.Reaction

class ReceivedMessageDelegateItem(
    override val id: Int,
    var value: ReceivedMessageModel
): MessageDelegateItem() {

    override val reactionList: List<Reaction>
        get() = value.reactionList

    override fun changeReactionList(newReactionList: List<Reaction>) {
        value = value.copy(reactionList = newReactionList)
    }

    override fun copy(): MessageDelegateItem {
        return ReceivedMessageDelegateItem(id = id, value = value)
    }

    override fun id(): Any = id
    override fun content(): Any = value

    override fun compareToOther(other: DelegateItem): Boolean {
        return (other as ReceivedMessageDelegateItem).value == this.value
    }
    override fun payload(other: Any): DelegateItem.Payloadable {
        if (other is ReceivedMessageDelegateItem) {
            if (value.reactionList != other.value.reactionList) {
                return ChangePayload.ReactionListChanged(other.value.reactionList)
            }
        }
        return DelegateItem.Payloadable.None
    }
}