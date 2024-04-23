package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat

import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store

class ChatStoreFactory(private val actor: ChatActor) {

    fun create(): Store<ChatEvent, ChatEffect, ChatState> {
        return ElmStore(
            initialState = ChatState.Initial,
            reducer = ChatReducer(),
            actor = actor
        )
    }
}