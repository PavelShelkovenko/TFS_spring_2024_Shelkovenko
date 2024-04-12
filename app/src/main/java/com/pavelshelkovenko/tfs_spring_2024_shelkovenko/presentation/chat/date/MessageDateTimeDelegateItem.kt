package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.date

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.delegate_adapter.DelegateItem

class MessageDateTimeDelegateItem(
    val id: Int,
    val value: MessageDateTime
): DelegateItem {
    override fun id(): Any = id
    override fun content(): Any = value

    override fun compareToOther(other: DelegateItem): Boolean {
        return (other as MessageDateTimeDelegateItem).value == this.value
    }
}