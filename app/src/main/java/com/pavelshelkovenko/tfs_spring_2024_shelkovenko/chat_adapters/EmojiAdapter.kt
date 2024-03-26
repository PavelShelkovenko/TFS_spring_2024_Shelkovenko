package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.chat_adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.models.Emoji
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.ItemEmojiBinding
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.toEmoji

class EmojiAdapter(private val emojis: List<Emoji>) :
    RecyclerView.Adapter<EmojiAdapter.ViewHolder>() {

    var onEmojiClickListener: ((String) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemEmojiBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(emojis[position])
    }

    override fun getItemCount(): Int = emojis.size

    inner class ViewHolder(private val binding: ItemEmojiBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(emoji: Emoji) {
            with(binding) {
                emojiCode.text = emoji.code.toEmoji()
                emojiCode.setOnClickListener {
                    onEmojiClickListener?.invoke(emoji.code)
                }
            }
        }
    }
}