package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.chat_adapters.date

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.chat_adapters.delegate_adapter.DelegateAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.chat_adapters.delegate_adapter.DelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.DateItemBinding

class DateAdapter :
    DelegateAdapter<DateDelegateItem, DateAdapter.ViewHolder>(DateDelegateItem::class.java) {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
       ViewHolder(
            DateItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(
        model: DateDelegateItem,
        viewHolder: ViewHolder,
        payloads: List<DelegateItem.Payloadable>
    ) {
        viewHolder.bind(model.content() as DateModel)
    }

    inner class ViewHolder(private val binding: DateItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: DateModel) {
            with(binding) {
                date.text = model.date
            }
        }
    }
}