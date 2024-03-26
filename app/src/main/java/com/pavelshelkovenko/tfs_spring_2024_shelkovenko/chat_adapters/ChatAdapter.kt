package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.chat_adapters

import android.util.SparseArray
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.chat_adapters.delegate_adapter.DelegateAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.chat_adapters.delegate_adapter.DelegateAdapterItemDiffCallback
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.chat_adapters.delegate_adapter.DelegateItem

@Suppress("UNCHECKED_CAST")
class ChatAdapter(
     val delegates: SparseArray<DelegateAdapter<DelegateItem, RecyclerView.ViewHolder>>
) : ListAdapter<DelegateItem, RecyclerView.ViewHolder>(DelegateAdapterItemDiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        for (i in 0 until delegates.size()) {
            if (delegates[i].modelClass == getItem(position).javaClass) {
                return delegates.keyAt(i)
            }
        }
        throw NullPointerException("Can not get viewType for position $position")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        delegates[viewType].onCreateViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        onBindViewHolder(holder, position, mutableListOf())

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        val delegateAdapter = delegates[getItemViewType(position)]

        if (delegateAdapter != null) {
            val delegatePayloads = payloads.map { it as DelegateItem.Payloadable }
            delegateAdapter.onBindViewHolder(getItem(position), holder, delegatePayloads)
        } else {
            throw NullPointerException("can not find adapter for position $position")
        }
    }

    class Builder {

        private var count: Int = 0
        private val delegates: SparseArray<DelegateAdapter<DelegateItem, RecyclerView.ViewHolder>> = SparseArray()

        fun add(delegateAdapter: DelegateAdapter<out DelegateItem, *>): Builder {
            delegates.put(count++, delegateAdapter as DelegateAdapter<DelegateItem, RecyclerView.ViewHolder>)
            return this
        }

        fun build(): ChatAdapter {
            require(count != 0) { "Register at least one adapter" }
            return ChatAdapter(delegates)
        }
    }
}