package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.people

import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store

class PeopleStoreFactory(private val actor: PeopleActor) {

    fun create(): Store<PeopleEvent, PeopleEffect, PeopleState> {
        return ElmStore(
            initialState = PeopleState.Initial,
            reducer = PeopleReducer(),
            actor = actor
        )
    }
}