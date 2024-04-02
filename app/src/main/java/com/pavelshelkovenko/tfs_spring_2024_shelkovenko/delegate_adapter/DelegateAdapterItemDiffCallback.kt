package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.delegate_adapter

import androidx.recyclerview.widget.DiffUtil

class DelegateAdapterItemDiffCallback : DiffUtil.ItemCallback<DelegateItem>() {

    override fun areItemsTheSame(oldItem: DelegateItem, newItem: DelegateItem): Boolean =
        oldItem::class == newItem::class && oldItem.id() == newItem.id()

    override fun areContentsTheSame(oldItem: DelegateItem, newItem: DelegateItem): Boolean =
        oldItem.compareToOther(newItem)

    override fun getChangePayload(oldItem: DelegateItem, newItem: DelegateItem): Any =
        oldItem.payload(newItem)
}