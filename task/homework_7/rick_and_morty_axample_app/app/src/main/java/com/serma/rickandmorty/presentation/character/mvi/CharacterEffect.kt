package com.serma.rickandmorty.presentation.character.mvi

import com.serma.rickandmorty.presentation.mvi.MviEffect

sealed interface CharacterEffect : MviEffect {
    data class ShowError(val throwable: Throwable) : CharacterEffect
}