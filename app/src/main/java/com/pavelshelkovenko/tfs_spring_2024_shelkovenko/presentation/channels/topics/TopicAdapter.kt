package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.topics

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.TopicItemBinding
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.delegate_adapter.DelegateAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.delegate_adapter.DelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Topic


class TopicAdapter : DelegateAdapter<TopicDelegateItem, TopicAdapter.ViewHolder>(TopicDelegateItem::class.java) {

    var onTopicClickListener: ((Topic) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(
            TopicItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(
        model: TopicDelegateItem,
        viewHolder: ViewHolder,
        payloads: List<DelegateItem.Payloadable>
    ) {
        viewHolder.bind(model.content() as Topic)
    }

    inner class ViewHolder(private val binding: TopicItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: Topic) {
            with(binding) {
                topicName.text = model.name
                topicContainer.setBackgroundColor(model.color)
                topicContainer.setOnClickListener {
                    onTopicClickListener?.invoke(model)
                }
            }
        }
    }
}