package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.profile.own

import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store


class OwnProfileStoreFactory(private val actor: OwnProfileActor) {

    fun create(): Store<OwnProfileEvent, OwnProfileEffect, OwnProfileState> {
        return ElmStore(
            initialState = OwnProfileState.Initial,
            reducer = OwnProfileReducer(),
            actor = actor
        )
    }
}