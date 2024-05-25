package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.topic_chooser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.TopicChooserItemBinding
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Topic
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.base.delegate_adapter.DelegateAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.base.delegate_adapter.DelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.channels.topics.TopicDelegateItem

class TopicChooserAdapter :
    DelegateAdapter<TopicDelegateItem, TopicChooserAdapter.ViewHolder>(TopicDelegateItem::class.java) {

    var onTopicClickListener: ((Topic) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(
            TopicChooserItemBinding.inflate(
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

    inner class ViewHolder(private val binding: TopicChooserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: Topic) {
            with(binding) {
                topicName.text = itemView.resources.getString(R.string.topic_chooser_prefix, model.name)
                topicChooserContainer.setOnClickListener {
                    onTopicClickListener?.invoke(model)
                }
            }
        }
    }
}