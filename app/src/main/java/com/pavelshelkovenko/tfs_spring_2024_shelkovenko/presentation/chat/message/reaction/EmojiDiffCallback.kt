package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message.reaction

import androidx.recyclerview.widget.DiffUtil
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Emoji

class EmojiDiffCallback : DiffUtil.ItemCallback<Emoji>() {
    override fun areItemsTheSame(oldItem: Emoji, newItem: Emoji): Boolean {
        return oldItem.emojiCode == newItem.emojiCode
    }

    override fun areContentsTheSame(oldItem: Emoji, newItem: Emoji): Boolean {
        return oldItem == newItem
    }

}