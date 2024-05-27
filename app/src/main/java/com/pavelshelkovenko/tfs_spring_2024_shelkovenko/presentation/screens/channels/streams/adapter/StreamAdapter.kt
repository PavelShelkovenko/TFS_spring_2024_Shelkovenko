package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.channels.streams.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.StreamItemBinding
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Stream
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.SubscriptionStatus
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.base.delegate_adapter.DelegateAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.base.delegate_adapter.DelegateItem

class StreamAdapter :
    DelegateAdapter<StreamDelegateItem, StreamAdapter.ViewHolder>(StreamDelegateItem::class.java) {

    var onStreamClickListener: ((StreamDelegateItem) -> Unit)? = null
    var onSubscriptionClickListener: ((StreamDelegateItem) -> Unit)? = null

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
                streamName.text = itemView.resources.getString(R.string.stream_name_prefix, stream.name)
                when(stream.subscriptionStatus) {
                    SubscriptionStatus.SUBSCRIBED -> {
                        subscribedButton.isVisible = true
                        unsubscribedButton.isVisible = false
                        subscribedButton.setOnClickListener {
                            onSubscriptionClickListener?.invoke(model)
                        }
                    }
                    SubscriptionStatus.UNSUBSCRIBED -> {
                        unsubscribedButton.isVisible = true
                        subscribedButton.isVisible = false
                        unsubscribedButton.setOnClickListener {
                            onSubscriptionClickListener?.invoke(model)
                        }
                    }
                }
                streamContainer.setOnClickListener {
                    onStreamClickListener?.invoke(model)
                }
                if (stream.isExpanded) {
                    openTopicsButton.isVisible = false
                    closeTopicsButton.isVisible = true
                    closeTopicsButton.setOnClickListener {
                        onStreamClickListener?.invoke(model)
                    }
                } else {
                    openTopicsButton.isVisible = true
                    closeTopicsButton.isVisible = false
                    openTopicsButton.setOnClickListener {
                        onStreamClickListener?.invoke(model)
                    }
                }
            }
        }
    }
}