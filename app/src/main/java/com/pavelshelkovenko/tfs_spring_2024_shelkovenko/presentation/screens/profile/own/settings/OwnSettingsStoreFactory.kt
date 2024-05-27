package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.profile.own.settings

import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store

class OwnSettingsStoreFactory(private val actor: OwnSettingsActor) {

    fun create(): Store<OwnSettingsEvent, OwnSettingsEffect, OwnSettingsState> {
        return ElmStore(
            initialState = OwnSettingsState.Initial,
            reducer = OwnSettingsReducer(),
            actor = actor
        )
    }
}