package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.chat_adapters.message.send_message

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.models.User
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.chat_adapters.delegate_adapter.DelegateAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.chat_adapters.delegate_adapter.DelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.SendMessageBinding
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.chat_adapters.message.MessageDelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.models.Reaction

class SendMessageAdapter :
    DelegateAdapter<SendMessageDelegateItem, SendMessageAdapter.ViewHolder>(
        SendMessageDelegateItem::class.java
    ) {

    var onMessageLongClickListener: ((Int) -> Unit)? = null
    var onAddIconClickListener: ((Int) -> Unit)? = null
    var onEmojiClickListener: ((Int, String) -> Unit)? = null
    var localUser: User? = null

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {

        return ViewHolder(
            SendMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        model: SendMessageDelegateItem,
        viewHolder: ViewHolder,
        payloads: List<DelegateItem.Payloadable>
    ) {
        when (val payload = payloads.firstOrNull() as? MessageDelegateItem.ChangePayload) {
            is MessageDelegateItem.ChangePayload.ReactionListChanged ->
                viewHolder.bindReactionList(model, payload.reactionList)

            else -> viewHolder.bind(model)
        }
    }

    inner class ViewHolder(private val binding: SendMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: SendMessageDelegateItem) {
            val messageModel = model.content() as SendMessageModel
            with(binding) {
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
            model: SendMessageDelegateItem,
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