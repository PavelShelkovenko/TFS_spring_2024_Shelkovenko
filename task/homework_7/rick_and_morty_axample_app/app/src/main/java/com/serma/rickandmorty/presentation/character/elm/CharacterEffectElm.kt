package com.serma.rickandmorty.presentation.character.elm

sealed interface CharacterEffectElm {
    data class ShowError(val throwable: Throwable) : CharacterEffectElm
}