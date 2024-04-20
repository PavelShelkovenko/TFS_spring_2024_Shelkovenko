package com.serma.rickandmorty.presentation.character.elm

import com.serma.rickandmorty.presentation.character.model.PersonUi
import com.serma.rickandmorty.presentation.character.mvi.CharacterIntent

sealed interface CharacterEvent {
    sealed interface Ui : CharacterEvent {
        data object Init : Ui
        data object NextPage : Ui
        data object PrevPage : Ui
        data object ReloadPage : Ui
    }

    sealed interface Domain : CharacterEvent {
        data class DataLoaded(val personUi: PersonUi, val page: Int) : Domain
        data class Error(val throwable: Throwable) : Domain
    }
}