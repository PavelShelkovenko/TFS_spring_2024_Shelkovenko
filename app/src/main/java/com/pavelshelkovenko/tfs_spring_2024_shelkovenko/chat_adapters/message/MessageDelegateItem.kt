package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.chat_adapters.message

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.chat_adapters.delegate_adapter.DelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.models.Reaction

abstract class MessageDelegateItem: DelegateItem {

    abstract val id: Int

    abstract val reactionList: List<Reaction>
    abstract fun changeReactionList(newReactionList: List<Reaction>)

    abstract fun copy(): MessageDelegateItem

    sealed class ChangePayload: DelegateItem.Payloadable {
        data class ReactionListChanged(val reactionList: List<Reaction>): ChangePayload()
    }
}