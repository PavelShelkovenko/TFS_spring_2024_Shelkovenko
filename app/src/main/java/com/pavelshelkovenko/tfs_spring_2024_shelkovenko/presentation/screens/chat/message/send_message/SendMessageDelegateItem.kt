package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.message.send_message

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Reaction
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.base.delegate_adapter.DelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.message.MessageDelegateItem

class SendMessageDelegateItem(
    override val id: Int,
    var value: SendMessageModel
): MessageDelegateItem() {

    override val reactionList: List<Reaction>
        get() = value.reactionList

    override fun changeReactionList(newReactionList: List<Reaction>) {
        value = value.copy(reactionList = newReactionList)
    }

    override fun copy(): MessageDelegateItem {
        return SendMessageDelegateItem(id = id, value = value)
    }

    override fun id(): Any = id
    override fun content(): Any = value

    override fun compareToOther(other: DelegateItem): Boolean {
        return (other as SendMessageDelegateItem).value == this.value
    }

    override fun payload(other: Any): DelegateItem.Payloadable {
        if (other is SendMessageDelegateItem) {
            if (value.reactionList != other.value.reactionList) {
                return ChangePayload.ReactionListChanged(other.value.reactionList)
            }
        }
        return DelegateItem.Payloadable.None
    }
}