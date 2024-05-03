package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.profile.another

import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store

class AnotherProfileStoreFactory(private val actor: AnotherProfileActor) {

    fun create(): Store<AnotherProfileEvent, AnotherProfileEffect, AnotherProfileState> {
        return ElmStore(
            initialState = AnotherProfileState.Initial,
            reducer = AnotherProfileReducer(),
            actor = actor
        )
    }
}