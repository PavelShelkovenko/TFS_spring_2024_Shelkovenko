package com.serma.rickandmorty.presentation.character.mvi

import com.serma.rickandmorty.presentation.mvi.MviIntent

sealed interface CharacterIntent : MviIntent {
    data object Init : CharacterIntent
    data object NextPage : CharacterIntent
    data object PrevPage : CharacterIntent
    data object ReloadPage : CharacterIntent
}