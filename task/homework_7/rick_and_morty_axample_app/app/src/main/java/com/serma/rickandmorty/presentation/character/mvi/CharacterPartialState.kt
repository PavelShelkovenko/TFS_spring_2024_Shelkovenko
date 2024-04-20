package com.serma.rickandmorty.presentation.character.mvi

import com.serma.rickandmorty.presentation.character.model.PersonUi
import com.serma.rickandmorty.presentation.mvi.MviPartialState

sealed interface CharacterPartialState: MviPartialState {
    data class NextPageLoaded(val personUi: PersonUi, val page: Int): CharacterPartialState
    data class PrevPageLoaded(val personUi: PersonUi, val page: Int): CharacterPartialState
}