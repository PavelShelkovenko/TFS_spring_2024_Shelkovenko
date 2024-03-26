package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.chat_adapters.delegate_adapter

interface DelegateItem {
    fun id(): Any

    fun content(): Any

    fun compareToOther(other: DelegateItem): Boolean

    fun payload(other: Any): Payloadable = Payloadable.None

    interface Payloadable {
        object None: Payloadable
    }
}