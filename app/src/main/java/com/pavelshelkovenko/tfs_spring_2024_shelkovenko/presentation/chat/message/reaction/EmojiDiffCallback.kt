package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message.reaction

import androidx.recyclerview.widget.DiffUtil

class EmojiDiffCallback : DiffUtil.ItemCallback<Emoji>() {
    override fun areItemsTheSame(oldItem: Emoji, newItem: Emoji): Boolean {
        return oldItem.code == newItem.code
    }

    override fun areContentsTheSame(oldItem: Emoji, newItem: Emoji): Boolean {
        return oldItem == newItem
    }

}