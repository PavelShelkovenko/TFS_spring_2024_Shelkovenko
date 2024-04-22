package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.delegate_adapter.DelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Reaction

abstract class MessageDelegateItem: DelegateItem {

    abstract val id: Int

    abstract val reactionList: List<Reaction>
    abstract fun changeReactionList(newReactionList: List<Reaction>)

    abstract fun copy(): MessageDelegateItem

    sealed class ChangePayload : DelegateItem.Payloadable {
        data class ReactionListChanged(val reactionList: List<Reaction>) : ChangePayload()
    }
}