package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.date

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.DateItemBinding
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.delegate_adapter.DelegateAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.delegate_adapter.DelegateItem

class MessageDateTimeAdapter :
    DelegateAdapter<MessageDateTimeDelegateItem, MessageDateTimeAdapter.ViewHolder>(MessageDateTimeDelegateItem::class.java) {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
       ViewHolder(
            DateItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(
        model: MessageDateTimeDelegateItem,
        viewHolder: ViewHolder,
        payloads: List<DelegateItem.Payloadable>
    ) {
        viewHolder.bind(model.value)
    }

    inner class ViewHolder(private val binding: DateItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: MessageDateTime) {
            binding.date.text = model.dateTime
        }
    }
}