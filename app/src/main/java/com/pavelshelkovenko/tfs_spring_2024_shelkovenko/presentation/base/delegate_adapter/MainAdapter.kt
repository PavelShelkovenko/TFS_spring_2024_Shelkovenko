package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.base.delegate_adapter

import android.util.Log
import android.util.SparseArray
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

@Suppress("UNCHECKED_CAST")
class MainAdapter(
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
        try {
            val delegatePayloads = payloads.map { it as DelegateItem.Payloadable }
            delegateAdapter.onBindViewHolder(getItem(position), holder, delegatePayloads)
        } catch (ex: Exception) {
            Log.e("MainAdapter", "${ex.message}")
        }
    }

    class Builder {

        private var count: Int = 0
        private val delegates: SparseArray<DelegateAdapter<DelegateItem, RecyclerView.ViewHolder>> = SparseArray()

        fun add(delegateAdapter: DelegateAdapter<out DelegateItem, *>): Builder {
            delegates.put(count++, delegateAdapter as DelegateAdapter<DelegateItem, RecyclerView.ViewHolder>)
            return this
        }

        fun build(): MainAdapter {
            require(count != 0) { "Register at least one adapter" }
            return MainAdapter(delegates)
        }
    }
}