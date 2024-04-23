package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.delegate_adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class DelegateAdapter<M, in VH : RecyclerView.ViewHolder>(val modelClass: Class<out M>) {

    abstract fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    abstract fun onBindViewHolder(
        model: M,
        viewHolder: VH,
        payloads: List<DelegateItem.Payloadable>
    )

}