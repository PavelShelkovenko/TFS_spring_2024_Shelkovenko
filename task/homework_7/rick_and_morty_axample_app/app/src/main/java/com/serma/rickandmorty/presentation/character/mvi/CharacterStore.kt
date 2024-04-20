package com.serma.rickandmorty.presentation.character.mvi

import com.serma.rickandmorty.presentation.mvi.MviStore
import com.serma.rickandmorty.presentation.utils.LceState

class CharacterStore(reducer: CharacterReducer, actor: CharacterActor) :
    MviStore<CharacterPartialState, CharacterIntent, CharacterState, CharacterEffect>(
        reducer,
        actor
    ) {
    override fun initialStateCreator(): CharacterState = CharacterState(personUi = LceState.Loading)
}