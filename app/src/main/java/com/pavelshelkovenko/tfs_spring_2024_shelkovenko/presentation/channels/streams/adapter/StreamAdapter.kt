package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.streams.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.StreamItemBinding
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Stream
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.delegate_adapter.DelegateAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.delegate_adapter.DelegateItem

class StreamAdapter :
    DelegateAdapter<StreamDelegateItem, StreamAdapter.ViewHolder>(StreamDelegateItem::class.java) {

    var onStreamClickListener: ((StreamDelegateItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(
            StreamItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(
        model: StreamDelegateItem,
        viewHolder: ViewHolder,
        payloads: List<DelegateItem.Payloadable>
    ) {
        viewHolder.bind(model)
    }

    inner class ViewHolder(private val binding: StreamItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: StreamDelegateItem) {
            val stream = model.content() as Stream
            with(binding) {
                streamName.text = stream.name
                streamContainer.setOnClickListener {
                    onStreamClickListener?.invoke(model)
                }
                if (stream.isExpanded) {
                    openTopicsButton.visibility = View.GONE
                    closeTopicsButton.visibility = View.VISIBLE
                    closeTopicsButton.setOnClickListener {
                        onStreamClickListener?.invoke(model)
                    }
                } else {
                    openTopicsButton.visibility = View.VISIBLE
                    closeTopicsButton.visibility = View.GONE
                    openTopicsButton.setOnClickListener {
                        onStreamClickListener?.invoke(model)
                    }
                }
            }
        }
    }
}