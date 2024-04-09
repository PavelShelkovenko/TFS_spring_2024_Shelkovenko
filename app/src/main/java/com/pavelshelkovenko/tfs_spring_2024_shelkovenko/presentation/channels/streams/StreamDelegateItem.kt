package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.streams

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.delegate_adapter.DelegateItem

class StreamDelegateItem(
    val id: Int,
    val value: Stream
): DelegateItem {
    override fun id(): Any = id
    override fun content(): Any = value

    override fun compareToOther(other: DelegateItem): Boolean {
        return (other as StreamDelegateItem).value == this.value
    }
}