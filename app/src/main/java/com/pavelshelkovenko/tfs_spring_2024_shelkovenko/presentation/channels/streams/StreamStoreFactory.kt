package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.streams

import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store

class StreamStoreFactory(private val actor: StreamActor) {

    fun create(): Store<StreamEvent, StreamEffect, StreamState> {
        return ElmStore(
            initialState = StreamState.Initial,
            reducer = StreamReducer(),
            actor = actor
        )
    }
}