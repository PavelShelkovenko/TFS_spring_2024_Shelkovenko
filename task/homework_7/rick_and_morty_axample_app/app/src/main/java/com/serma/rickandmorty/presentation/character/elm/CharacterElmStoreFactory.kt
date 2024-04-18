package com.serma.rickandmorty.presentation.character.elm

import com.serma.rickandmorty.presentation.utils.LceState
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store

class CharacterElmStoreFactory(private val actorElm: CharacterActorElm) {

    fun create(): Store<CharacterEvent, CharacterEffectElm, CharacterStateElm> {
        return ElmStore(
            initialState = CharacterStateElm(personUi = LceState.Loading),
            reducer = CharacterReducer(),
            actor = actorElm
        )
    }
}