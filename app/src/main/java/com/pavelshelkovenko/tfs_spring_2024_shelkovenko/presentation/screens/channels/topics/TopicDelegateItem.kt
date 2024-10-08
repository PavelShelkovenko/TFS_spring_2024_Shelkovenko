package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.channels.topics

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Topic
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.base.delegate_adapter.DelegateItem

class TopicDelegateItem(
    val id: Int,
    val value: Topic
): DelegateItem {
    override fun id(): Any = id
    override fun content(): Any = value

    override fun compareToOther(other: DelegateItem): Boolean {
        return (other as TopicDelegateItem).value == this.value
    }
}