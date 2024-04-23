package com.serma.rickandmorty.presentation.character.mvi

import com.serma.rickandmorty.presentation.character.model.PersonUi
import com.serma.rickandmorty.presentation.mvi.MviReducer
import com.serma.rickandmorty.presentation.utils.LceState

class CharacterReducer : MviReducer<CharacterPartialState, CharacterState> {
    override fun reduce(
        prevState: CharacterState,
        partialState: CharacterPartialState
    ): CharacterState {
        return when (partialState) {
            is CharacterPartialState.NextPageLoaded -> updatePageInState(
                prevState,
                partialState.page,
                partialState.personUi
            )

            is CharacterPartialState.PrevPageLoaded -> updatePageInState(
                prevState,
                partialState.page,
                partialState.personUi
            )
        }
    }

    private fun updatePageInState(
        prevState: CharacterState,
        page: Int,
        personUi: PersonUi
    ): CharacterState {
        return prevState.copy(page = page, personUi = LceState.Content(personUi))
    }

}