package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.chat_adapters.date

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.chat_adapters.delegate_adapter.DelegateItem

class DateDelegateItem(
    val id: Int,
    private val value: DateModel
): DelegateItem {
    override fun id(): Any = id

    override fun content(): Any = value

    override fun compareToOther(other: DelegateItem): Boolean {
        return (other as DateDelegateItem).value == content()
    }
}