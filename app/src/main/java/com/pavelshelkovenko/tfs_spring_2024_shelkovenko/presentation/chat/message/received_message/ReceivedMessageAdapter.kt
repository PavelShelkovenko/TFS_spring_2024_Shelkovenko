package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message.received_message

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.ReceivedMessageBinding
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.delegate_adapter.DelegateAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.delegate_adapter.DelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Reaction
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message.MessageDelegateItem

class ReceivedMessageAdapter :
    DelegateAdapter<ReceivedMessageDelegateItem, ReceivedMessageAdapter.ViewHolder>(
        ReceivedMessageDelegateItem::class.java
    ) {

    var onMessageLongClickListener: ((Int) -> Unit)? = null
    var onAddIconClickListener: ((Int) -> Unit)? = null
    var onEmojiClickListener: ((Int, Reaction) -> Unit)? = null
    var localUserId: Int? = null

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
            val messageModel = model.value
            with(binding) {
                Glide.with(itemView).load(messageModel.avatarUrl).into(messageGroup.userAvatar)
                messageGroup.setUserName(messageModel.userName)
                messageGroup.setTextMessage(messageModel.textMessage)
                messageGroup.setReactionList(
                    localUserId,
                    messageModel.reactionList,
                    onEmojiClick = { reaction ->
                        onEmojiClickListener?.invoke(model.id, reaction)
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
                localUserId,
                newReactionList,
                onEmojiClick = { reaction ->
                    onEmojiClickListener?.invoke(model.id, reaction)
                },
                onAddIconClick = {
                    onAddIconClickListener?.invoke(model.id)
                }
            )
        }
    }
}