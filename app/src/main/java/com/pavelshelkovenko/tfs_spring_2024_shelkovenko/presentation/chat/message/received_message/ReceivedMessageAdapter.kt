package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message.received_message

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.models.User
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.delegate_adapter.DelegateAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.delegate_adapter.DelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.ReceivedMessageBinding
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message.MessageDelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message.reaction.Reaction

class ReceivedMessageAdapter :
    DelegateAdapter<ReceivedMessageDelegateItem, ReceivedMessageAdapter.ViewHolder>(
        ReceivedMessageDelegateItem::class.java
    ) {

    var onMessageLongClickListener: ((Int) -> Unit)? = null
    var onAddIconClickListener: ((Int) -> Unit)? = null
    var onEmojiClickListener: ((Int, String) -> Unit)? = null
    var localUser: User? = null

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ViewHolder(
            ReceivedMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        model: ReceivedMessageDelegateItem,
        viewHolder: ViewHolder,
        payloads: List<DelegateItem.Payloadable>
    ) {
        when (val payload = payloads.firstOrNull() as? MessageDelegateItem.ChangePayload) {
            is MessageDelegateItem.ChangePayload.ReactionListChanged ->
                viewHolder.bindReactionList(model, payload.reactionList)

            else -> viewHolder.bind(model)
        }
    }

    inner class ViewHolder(private val binding: ReceivedMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: ReceivedMessageDelegateItem) {
            val messageModel = model.content() as ReceivedMessageModel
            with(binding) {
                messageGroup.setUserAvatar(messageModel.userAvatar)
                messageGroup.setUserName(messageModel.userName)
                messageGroup.setTextMessage(messageModel.textMessage)
                messageGroup.setReactionList(
                    localUser?.id,
                    messageModel.reactionList,
                    onEmojiClick = { emojiCode ->
                        onEmojiClickListener?.invoke(model.id, emojiCode)
                    },
                    onAddIconClick = {
                        onAddIconClickListener?.invoke(model.id)
                    }
                )
                messageGroup.getTextMessageChild().setOnLongClickListener {
                    onMessageLongClickListener?.invoke(model.id)
                    true
                }
            }
        }

        fun bindReactionList(
            model: ReceivedMessageDelegateItem,
            newReactionList: List<Reaction>
        ) {
            binding.messageGroup.setReactionList(
                localUser?.id,
                newReactionList,
                onEmojiClick = { emojiCode ->
                    onEmojiClickListener?.invoke(model.id, emojiCode)
                },
                onAddIconClick = {
                    onAddIconClickListener?.invoke(model.id)
                }
            )
        }
    }
}