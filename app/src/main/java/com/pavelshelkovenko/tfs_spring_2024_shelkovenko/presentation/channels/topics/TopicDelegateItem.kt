package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.topics

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.delegate_adapter.DelegateItem

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