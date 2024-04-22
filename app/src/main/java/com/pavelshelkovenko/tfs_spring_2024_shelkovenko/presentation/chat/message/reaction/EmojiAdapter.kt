package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message.reaction

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.EmojiItemBinding
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Emoji
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.toEmoji

class EmojiAdapter(private val emojis: List<Emoji>) :
    ListAdapter<Emoji, EmojiAdapter.ViewHolder>(EmojiDiffCallback()) {

    var onEmojiClickListener: ((Emoji) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            EmojiItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(emojis[position])
    }

    override fun getItemCount(): Int = emojis.size

    inner class ViewHolder(private val binding: EmojiItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(emoji: Emoji) {
            with(binding) {
                emojiCode.text = emoji.emojiCode.toEmoji()
                emojiCode.setOnClickListener {
                    onEmojiClickListener?.invoke(emoji)
                }
            }
        }
    }
}